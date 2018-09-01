package com.exercicioiesb.casa.exerciciofinal

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.exercicioiesb.casa.exerciciofinal.entity.Usuario
import kotlinx.android.synthetic.main.activity_perfilusuario.*
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.widget.Toast
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.*


class PerfilUsuarioActivity : AppCompatActivity(){

    private var imgUri: Uri? = null

    private var CAMERA = 0
    private var GALLERY = 1
    private var imagemTexto : String = ""


    companion object{
        const val REQUEST_PERMISSION = 1
    }

    var mAuth : FirebaseAuth? = null
    var storageReference: StorageReference? = null
    var key : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfilusuario)

        mAuth = FirebaseAuth.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        val dbFire = FirebaseDatabase.getInstance()
        val usuarioRef = dbFire.getReference()

        val uid = mAuth!!.currentUser!!.uid

        usuarioRef.child("usuarios").orderByChild("email").equalTo(mAuth!!.currentUser!!.email).addValueEventListener(object: ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot){

                for(usuarioSnapshot:DataSnapshot in dataSnapshot.children) {
                    edtNomeUsuario.text = Editable.Factory.getInstance().newEditable(usuarioSnapshot.child("nome").value.toString())
                    edtMatricula.text = Editable.Factory.getInstance().newEditable(usuarioSnapshot.child("matricula").value.toString())
                    edtTelefone.text = Editable.Factory.getInstance().newEditable(usuarioSnapshot.child("telefone").value.toString())
                    key = usuarioSnapshot.key
                    Log.i("Perfilusuario", key)
                }

            }

            override fun onCancelled(error: DatabaseError) {}
        })

        avatar.setOnClickListener {
            exibirEscolhaOrigem()
        }

        val it = intent

        var email = it.getStringExtra("email")

        btnSalvar.setOnClickListener{

            val usuario = Usuario()

            val util : Util = Util()

            if(imgUri==null){
                Toast.makeText(this, "Selecione uma imagem", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if(edtNomeUsuario.text.isEmpty() || edtMatricula.text.isEmpty() || edtTelefone.text.isEmpty()){
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            usuario.nome = edtNomeUsuario.text.toString()
            usuario.matricula = edtMatricula.text.toString()
            usuario.telefone = edtTelefone.text.toString()
            usuario.email = email

//            val usuarioRef = dbFire.getReference("/usuarios/${UUID.randomUUID()}")
            if(key.equals("")){
                key = UUID.randomUUID().toString()
            }
            val usuarioRef = dbFire.getReference("/usuarios/${key}")
            usuarioRef.setValue(usuario)

            val montaImagemReferencia = storageReference?.child("fotoPerfilUsusario/${key}.jpg")

            val uploadTask = montaImagemReferencia?.putFile(imgUri!!)

            uploadTask?.addOnFailureListener {
                Toast.makeText(this, "Não foi possível atualizar a foto do perfil!", Toast.LENGTH_LONG).show()
            }?.addOnSuccessListener { taskSnapshot ->
                val downloadUrl = taskSnapshot.uploadSessionUri
                Toast.makeText(this, "Perfil atualizado!", Toast.LENGTH_LONG).show()
            }


        }

    }

    internal fun exibirEscolhaOrigem() {

        val alert = AlertDialog.Builder(this@PerfilUsuarioActivity)
        alert.setMessage("Selecione origem")
                .setTitle(R.string.app_name)
                .setPositiveButton("Câmera", DialogInterface.OnClickListener { dialogInterface, i ->
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_PERMISSION)
                    } else {
                        acionarCamera()
                    }
                })

                .setNegativeButton("Galeria", DialogInterface.OnClickListener { dialogInterface, i ->
                    if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_PERMISSION)
                    } else {
                        acionarGaleria()
                    }
                })

                .setNeutralButton("Cancelar", null)
                .setIcon(R.drawable.logo_iesb_1)
        alert.create().show()
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode){
            REQUEST_PERMISSION -> {
                if( permissions.contains(Manifest.permission.CAMERA) && grantResults.contains(PackageManager.PERMISSION_GRANTED) ){
                    acionarCamera()
                }
            }
        }
    }

    private fun acionarCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if(intent.resolveActivity(packageManager) != null){
            var img: File? = null
            imgUri = null
            try{
                img = criarArquivoImagem()
            }catch(e: IOException){}

            if(img != null){
                imgUri = FileProvider.getUriForFile(
                        this,
                        "com.example.android.fileprovider",
                        img
                )
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri)
            }
        }

        startActivityForResult(intent, CAMERA)
    }

    fun acionarGaleria() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Selecionar Imagem"), GALLERY)
    }

    private fun criarArquivoImagem(): File? {
        val fileName = "avatar"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                fileName,
                ".png",
                storageDir
        )

        return image
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        var thumbnail: Bitmap
        if(requestCode == CAMERA && resultCode == Activity.RESULT_OK) {

            Picasso.get().load(imgUri).resize(300, 300).centerCrop().into(avatar)

        }else if(requestCode == GALLERY){
//            val imagemSelecionada = data?.getData()
            imgUri = data?.getData()
            Picasso.get().load(imgUri).resize(300, 300).centerCrop().into(avatar)
        }
    }

    private fun deslogar() {

        mAuth?.signOut()

        val intent = Intent(this@PerfilUsuarioActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()

    }

}