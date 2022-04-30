package com.example.androidproject01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.androidproject01.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    /**
     * Parse the price attribute
     */
    private fun parsePrice(field: Editable): Double {
        return if (field.isNotEmpty()) field.toString().toDouble() else 0.0
    }

    /**
     * Set the list data
     */
    private fun setListData(product: Product) {
        Log.d(localClassName, product.toString())
        binding.txtName.text = product.name
        binding.txtDescription.text = product.description
        binding.txtCode.text = product.code
        binding.txtPrice.text = product.price.toString()
    }

    /**
     * Listener on create event
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(localClassName, "onCreate")

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.btnSave.setOnClickListener {

            if (binding.edtName.text.isNotEmpty()) {

                // Form the product obj
                val product = Product(
                    binding.edtName.text?.toString(),
                    binding.edtDescription.text?.toString(),
                    binding.edtCode.text?.toString(),
                    parsePrice(binding.edtPrice.text)
                )

                // Form the product obj
                this.setListData(product)
            } else {
                Toast.makeText(
                    this,
                    "Please, enter the name",
                    Toast.LENGTH_LONG
                ).show()
            }

        }
    }

    /**
     * Save the state
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(localClassName, "onSaveInstanceState")

        // Form the product obj
        val product = Product(
            binding.edtName.text?.toString(),
            binding.edtDescription.text?.toString(),
            binding.edtCode.text?.toString(),
            parsePrice(binding.edtPrice.text)
        )

        // Save the obj in the state
        outState.putSerializable("product", product)
    }

    /**
     * Restore the state
     */
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.d(localClassName, "onRestoreInstanceState")

        // Get the product saved in the state
        val product = savedInstanceState.getSerializable("product") as Product

        Log.d("onRestoreInstanceState", product.toString())

        // Set the list data
        this.setListData(product)
    }

    /**
     * Listener on start event
     */
    override fun onStart() {
        super.onStart()
        Log.d(localClassName, "onStart")
    }

    /**
     * Listener on resume event
     */
    override fun onResume() {
        super.onResume()
        Log.d(localClassName, "onResume")
    }

    /**
     * Listener on pause event
     */
    override fun onPause() {
        super.onPause()
        Log.d(localClassName, "onPause")
    }

    /**
     * Listener on stop event
     */
    override fun onStop() {
        super.onStop()
        Log.d(localClassName, "onStop")
    }

    /**
     * Listener on restart event
     */
    override fun onRestart() {
        super.onRestart()
        Log.d(localClassName, "onRestart")
    }

    /**
     * Listener on destroy event
     */
    override fun onDestroy() {
        super.onDestroy()
        Log.d(localClassName, "onDestroy")
    }
}