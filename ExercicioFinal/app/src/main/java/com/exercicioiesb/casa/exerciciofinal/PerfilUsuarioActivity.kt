package com.exercicioiesb.casa.exerciciofinal

import android.Manifest
import android.app.Activity
import android.arch.persistence.room.Room
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.exercicioiesb.casa.exerciciofinal.dao.AppDatabase
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
import android.util.Base64
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.*


class PerfilUsuarioActivity : AppCompatActivity(){

    private var imgPath: String = ""

    private var CAMERA = 0
    private var GALLERY = 1
    private var imagemTexto : String = ""

    companion object{
        const val REQUEST_PERMISSION = 1
    }

    var mAuth : FirebaseAuth? = null
    var key : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfilusuario)

        mAuth = FirebaseAuth.getInstance()
        val dbFire = FirebaseDatabase.getInstance()
        val usuarioRef = dbFire.getReference()

//        val uid = mAuth!!.currentUser!!.uid

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

//        if(usuario != null) {
//
//            Log.i("Perfilusuario", "encontrado? "+usuario.email+" "+usuario.senha+" "+usuario.uid.toString())
//            if(!usuario.nome.isNullOrBlank())
//                edtNomeUsuario.text = Editable.Factory.getInstance().newEditable(usuario.nome)
//
//            if(!usuario.matricula.isNullOrBlank())
//                edtMatricula.text = Editable.Factory.getInstance().newEditable(usuario.matricula)
//
//            if(!usuario.telefone.isNullOrBlank())
//                edtTelefone.text = Editable.Factory.getInstance().newEditable(usuario.telefone)
//
//        }else{
//            Log.i("Perfilusuario", "não encontrado")
//        }
        //Não faz sentido preencher os campos senha e confirma senha nesse momento.

        btnSalvar.setOnClickListener{

            val usuario = Usuario()

            val util : Util = Util()

            if(imgPath==""){
                Toast.makeText(this, "Selecione uma imagem", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if(edtNomeUsuario.text.isEmpty() || edtMatricula.text.isEmpty() || edtTelefone.text.isEmpty() || edtSenha.text.isEmpty() || edtConfirmarSenha.text.isEmpty()){
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if(!util.comparaSenhas(edtSenha.text.toString(), edtConfirmarSenha.text.toString())){
                Toast.makeText(this, "Senhas são diferentes", Toast.LENGTH_LONG).show()
                edtSenha.setText("")
                edtConfirmarSenha.setText("")
                return@setOnClickListener
            }

            if(!util.senhaValida(edtSenha.text.toString())){
                Toast.makeText(this, "A senha deve conter 6 dígitos, sendo pelo menos um caractere maiúsculo, um caractere especial e um número", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

//            var u : Usuario = Usuario()
//            u.uid = usuario.uid
//            u.email = email
//            u.nome = edtNomeUsuario.text.toString()
//            u.matricula = edtMatricula.text.toString()
//            u.telefone = edtTelefone.text.toString()
//            u.senha = edtSenha.text.toString()
//            u.foto = imgPath

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

            Toast.makeText(this, "Perfil atualizado no firebase", Toast.LENGTH_LONG).show()

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
                        takePhotoFromCamera()
                    }
                })

                .setNegativeButton("Galeria", DialogInterface.OnClickListener { dialogInterface, i ->
                    if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_PERMISSION)
                    } else {
                        openGallery()
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
                    takePhotoFromCamera()
                }
            }
        }
    }

    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if(intent.resolveActivity(packageManager) != null){
            var img: File? = null
            try{
                img = createImageFile()
            }catch(e: IOException){}

            if(img != null){
                val imgUri = FileProvider.getUriForFile(
                        this,
                        "com.example.android.fileprovider",
                        img
                )
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri)
            }
        }

        startActivityForResult(intent, CAMERA)
    }

    private fun createImageFile(): File? {
        val fileName = "avatar"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                fileName,
                ".png",
                storageDir
        )
        imgPath = image.absolutePath

        return image
    }

    fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Selecionar Imagem"), GALLERY)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        var thumbnail: Bitmap
        if(requestCode == CAMERA && resultCode == Activity.RESULT_OK) {

            avatar.rotation = 90f
            avatar.setImageURI(Uri.parse(imgPath))
            Log.i("Caminho", imgPath)

//            thumbnail = data!!.extras!!.get("data") as Bitmap
//            avatar.setImageBitmap(thumbnail)
//
//            val outByte = ByteArrayOutputStream()
//
//            thumbnail.compress(Bitmap.CompressFormat.PNG, 100, outByte)
//
//            imagemTexto = Base64.encodeToString(outByte.toByteArray(), Base64.DEFAULT)

            //  Toast.makeText(this@MainActivity, "Foto capturada!", Toast.LENGTH_SHORT).show()

            //Toast.makeText(this@MainActivity, "Base64: " + base64, Toast.LENGTH_SHORT).show()
        }else if(requestCode == GALLERY){
            thumbnail = MediaStore.Images.Media.getBitmap(applicationContext.contentResolver, data?.getData())
            var scaledBm = scaleBitmapToMaxSize(600, thumbnail)
            avatar.setImageBitmap(scaledBm)
        }
    }

    fun scaleBitmapToMaxSize(maxSize: Int, bm: Bitmap): Bitmap {
        val outWidth: Int
        val outHeight: Int
        val inWidth = bm.width
        val inHeight = bm.height
        if (inWidth > inHeight) {
            outWidth = maxSize
            outHeight = inHeight * maxSize / inWidth
        } else {
            outHeight = maxSize
            outWidth = inWidth * maxSize / inHeight
        }
        return Bitmap.createScaledBitmap(bm, outWidth, outHeight, false)
    }

    private fun deslogar() {

        mAuth?.signOut()

        val intent = Intent(this@PerfilUsuarioActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()

    }

}