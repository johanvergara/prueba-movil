package com.app.clientesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TableLayout
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    var txtNombre:EditText?= null
    var txtNumeroIdentificacion:EditText?= null
    var txtTipoIdentificacion:EditText?= null
    var txtGenero:EditText?= null
    var tbClientes:TableLayout?=null
    var idGlobal:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        txtNombre=findViewById(R.id.txtNombre)
        txtNumeroIdentificacion=findViewById(R.id.txtNumeroIdentificacion)
        txtTipoIdentificacion=findViewById(R.id.txtTipoIdentificacion)
        txtGenero=findViewById(R.id.txtGenero)
        tbClientes=findViewById(R.id.tbClientes)
        cargarTabla()
    }

    fun cargarTabla() {
        tbClientes?.removeAllViews()
//        var queue=Volley.newRequestQueue(this)
//        var url = "http://172.27.128.1:3000/api/v1/clientes"
//        var jsonObjectRequest=JsonObjectRequest(Request.Method.GET,url,null,
//            Response.Listener { response ->
//                try {
//                    var jsonArray:JSONArray=response.getJSONArray("data");
//                    for (i in 0 until jsonArray.length()) {
//                        var jsonObject: JSONObject=jsonArray.getJSONObject(i)
//                        val registro:View=LayoutInflater.from(this).inflate(R.layout.table_row_cliente,null,false)
//                        val rowNombre:TextView=registro.findViewById<View>(R.id.rowNombre) as TextView
//                        val rowNumeroIdentificacion:TextView=registro.findViewById<View>(R.id.rowNumeroIdentificacion) as TextView
//                        val rowEditar:View=registro.findViewById<View>(R.id.rowEditar)
//                        val rowEliminar:View=registro.findViewById<View>(R.id.rowEliminar)
//                        rowNombre.text=jsonObject.getString("nombre")
//                        rowNumeroIdentificacion.text=jsonObject.getString("numero_identificacion")
//                        rowEditar.id=jsonObject.getString("id").toInt()
//                        rowEliminar.id=jsonObject.getString("id").toInt()
//                        tbClientes?.addView(registro)
//                    }
//                } catch (e: JSONException) {
//                    e.printStackTrace()
//                }
//            }, Response.ErrorListener { error ->
//                Toast.makeText(this, "Error al listar los clientes $error", Toast.LENGTH_LONG).show()
//            }
//        )
//        Log.i(jsonObjectRequest.toString(), jsonObjectRequest.toString())
//        queue.add(jsonObjectRequest)

        for (i in 0 until 5) {
            val registro:View=LayoutInflater.from(this).inflate(R.layout.table_row_cliente,null,false)
            val rowNombre:TextView=registro.findViewById<View>(R.id.rowNombre) as TextView
            val rowNumeroIdentificacion:TextView=registro.findViewById<View>(R.id.rowNumeroIdentificacion) as TextView
            val rowEditar:View=registro.findViewById<View>(R.id.rowEditar)
            val rowEliminar:View=registro.findViewById<View>(R.id.rowEliminar)
            rowNombre.text="Nombre $i"
            rowNumeroIdentificacion.text="Identificacion $i"
            rowEditar.id=i
            rowEliminar.id=i
            tbClientes?.addView(registro)
        }
    }

    fun clickTableEditar(view: View) {
        idGlobal=view.id.toString()
        val queue=Volley.newRequestQueue(this)
        var url = "http://172.27.128.1:3000/api/v1/clientes/${idGlobal}"
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
//        Toast.makeText(this,view.id.toString(), Toast.LENGTH_LONG).show()
    }

    fun clickGuardarEditar(view: View) {
        var url = "http://172.27.128.1:3000/api/v1/clientes/${idGlobal}"
        var queue=Volley.newRequestQueue(this)
        var resultado = object : StringRequest(Request.Method.PATCH,url,
            Response.Listener { response ->
                Toast.makeText(this, "El cliente fue actualizado exitosamente", Toast.LENGTH_LONG).show()
                cargarTabla()
            }, Response.ErrorListener { error ->
                Toast.makeText(this, "Error al actualizar el cliente, $error", Toast.LENGTH_LONG).show()
            }
        ){
            override fun getParams(): MutableMap<String, String> {
                val parametros=HashMap<String, String>()
                parametros.put("id", idGlobal!!)
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

    fun clickTableEliminar(view: View) {
        var url = "http://172.27.128.1:3000/api/v1/clientes/${view.id.toString()}/?eliminado=1"
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
                parametros.put("id", view.id.toString()!!)
                return parametros
            }
        }
        Log.i(String(), toString());
        Log.i(resultado.toString(), resultado.toString())
        queue.add(resultado)

//        Toast.makeText(this,view.id.toString(), Toast.LENGTH_LONG).show()
    }

    fun clickBtnGuardar(view:View) {
        var url = "http://172.27.128.1:3000/api/v1/clientes"
        val queue=Volley.newRequestQueue(this)
        var resultadoPost=object : StringRequest(Request.Method.POST,url,
        Response.Listener<String> { response ->
            Toast.makeText(this, "Cliente guardado correctamente", Toast.LENGTH_LONG).show()
        },Response.ErrorListener { error ->
                Toast.makeText(this, "Error al crear el cliente $error", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String> {
                val parametros=HashMap<String,String>()
                parametros.put("nombre", txtNombre?.text.toString())
                parametros.put("numero_identificacion", txtNumeroIdentificacion?.text.toString())
                parametros.put("tipo_identificacion", txtTipoIdentificacion?.text.toString())
                parametros.put("genero", txtGenero?.text.toString())
                return parametros;
            }
        }
        queue.add(resultadoPost);
    }

    fun clickRest(view: View) {
        txtNombre?.setText("")
        txtNumeroIdentificacion?.setText("")
        txtTipoIdentificacion?.setText("")
        txtGenero?.setText("")
    }
}
