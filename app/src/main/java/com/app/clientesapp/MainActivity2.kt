package com.app.clientesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class MainActivity2 : AppCompatActivity() {
    var txtNombre: EditText?= null
    var txtNumeroIdentificacion: EditText?= null
    var txtTipoIdentificacion: EditText?= null
    var txtGenero: EditText?= null
    var tvId: TextView?=null
    var id:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        txtNombre=findViewById(R.id.txtNombre)
        txtNumeroIdentificacion=findViewById(R.id.txtNumeroIdentificacion)
        txtTipoIdentificacion=findViewById(R.id.txtTipoIdentificacion)
        txtGenero=findViewById(R.id.txtGenero)
        id=intent.getStringExtra("id").toString()
        tvId?.setText(id)
        val queue=Volley.newRequestQueue(this)
        var url = "http://172.27.128.1:3000/api/v1/clientes/$id"
        val jsonObjectRequest= JsonObjectRequest(
            Request.Method.GET,url,null,
            Response.Listener { response ->
                txtNombre?.setText(response.getString("nombre"))
                txtNumeroIdentificacion?.setText(response.getString("numero_identificacion"))
                txtTipoIdentificacion?.setText(response.getString("tipo_identificacion_id"))
                txtGenero?.setText(response.getString("genero_id"))
            },Response.ErrorListener { error ->
                Toast.makeText(this,error.toString(),Toast.LENGTH_LONG).show()
            }
        )
        Log.i(String(), toString());
        Log.i(jsonObjectRequest.toString(), jsonObjectRequest.toString())
        queue.add(jsonObjectRequest)
    }
    fun clickRegresar(view: View) {
        var intent= Intent(this,MainActivity::class.java)
        startActivity(intent)
    }

    fun clickEliminar(view: View) {
        var url = "http://172.27.128.1:3000/api/v1/clientes/$id/?eliminado=1"
        var queue=Volley.newRequestQueue(this)
        var resultado = object : StringRequest(Request.Method.DELETE,url,
        Response.Listener { response ->
            Toast.makeText(this,"El cliente fue eliminado exitosamente", Toast.LENGTH_LONG).show()
        }, Response.ErrorListener { error ->
                Toast.makeText(this,"Error al eliminar el cliente $error", Toast.LENGTH_LONG).show()
            }
        ){
            override fun getParams(): MutableMap<String, String> {
                val parametros=HashMap<String, String>()
                parametros.put("id", id!!)
                return parametros
            }
        }
        Log.i(String(), toString());
        Log.i(resultado.toString(), resultado.toString())
        queue.add(resultado)
    }

    fun clickEditar(view: View) {
        var url = "http://172.27.128.1:3000/api/v1/clientes/$id"
        var queue=Volley.newRequestQueue(this)
        var resultado = object : StringRequest(Request.Method.PATCH,url,
        Response.Listener { response ->
            Toast.makeText(this, "El cliente fue actualizado exitosamente", Toast.LENGTH_LONG).show()
        }, Response.ErrorListener { error ->
                Toast.makeText(this, "Error al actualizar el cliente, $error", Toast.LENGTH_LONG).show()
            }
            ){
            override fun getParams(): MutableMap<String, String> {
                val parametros=HashMap<String, String>()
                parametros.put("id", id!!)
                parametros.put("nombre", txtNombre?.text.toString())
                parametros.put("numero_identificacion", txtNumeroIdentificacion?.text.toString())
                parametros.put("tipo_identificacion", txtTipoIdentificacion?.text.toString())
                parametros.put("genero", txtGenero?.text.toString())
                return parametros
            }
        }
        Log.i(String(), toString());
        Log.i(resultado.toString(), resultado.toString())
        queue.add(resultado)
    }
}