package com.codingwithjks.retrofithttprequest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.codingwithjks.retrofithttprequest.data.Bus
import com.codingwithjks.retrofithttprequest.data.adapter.BusAdapter
import com.codingwithjks.retrofithttprequest.databinding.ActivityMainBinding
import com.codingwithjks.retrofithttprequest.databinding.OpenDialogBinding
import com.codingwithjks.retrofithttprequest.ui.MainViewModel
import com.codingwithjks.retrofithttprequest.util.ApiState
import com.codingwithjks.retrofithttprequest.util.Listener
import com.codingwithjks.retrofithttprequest.util.showMsg
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), Listener {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var busAdapter: BusAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerview()
        getBusData()
        postBus()
    }

    private fun postBus() {
        binding.apply {
            save.setOnClickListener {
                if (!TextUtils.isEmpty(busNo.text.toString())
                    && !TextUtils.isEmpty(town.text.toString())
                ) {
                    lifecycleScope.launchWhenStarted {
                        mainViewModel.postBus(
                            busNo.text.toString().trim(), town.text.toString()
                        ).catch { e ->
                            showMsg("${e.message}")
                        }.collect { response ->
                            getBusData()
                            showMsg("data added successfully")
                            Log.d("main", "postBus: $response")
                        }
                    }
                } else {
                    showMsg("please fill all the field")
                }
            }
        }
    }

    private fun getBusData() {
        mainViewModel.getAllData()
        lifecycleScope.launchWhenStarted {
            mainViewModel.busData.collect {
                binding.apply {
                    when (it) {
                        is ApiState.Success -> {
                            recyclerview.isVisible = true
                            progressbar.isVisible = false
                            busAdapter.submitList(it.data)
                        }
                        is ApiState.Failure -> {
                            recyclerview.isVisible = false
                            progressbar.isVisible = false
                            Log.d("main", "getBusData: ${it.msg} ")
                        }
                        is ApiState.Loading -> {
                            recyclerview.isVisible = false
                            progressbar.isVisible = true
                        }
                        is ApiState.Empty -> {

                        }
                    }
                }
            }
        }
    }

    private fun initRecyclerview() {
        busAdapter = BusAdapter(this)
        binding.apply {
            recyclerview.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = busAdapter
            }
        }
    }

    override fun onClick(position: Int, busId: String) {
        lifecycleScope.launchWhenStarted {
            mainViewModel.delete(busId).catch { e ->
                showMsg("${e.message}")
                Log.d("main", "onClick: ${e.message}")
            }.collect {
                showMsg("successfully deleted..")
                getBusData()
            }
        }
    }

    override fun openDialog(position: Int, busId: String, town: String) {
        val alertDialog = AlertDialog.Builder(this)
        val itemView = OpenDialogBinding.inflate(LayoutInflater.from(this))
        val dialog = alertDialog.create()
        dialog.setView(itemView.root)
        itemView.apply {
            busNo.setText(busId)
            towns.setText(town)
            save.setOnClickListener {
                if (!TextUtils.isEmpty(busNo.text.toString()) && !TextUtils.isEmpty(towns.text.toString())) {
                    lifecycleScope.launchWhenStarted {
                        mainViewModel.update(busId,busNo.text.toString().trim(),towns.text.toString()).catch { e->
                            showMsg("${e.message}")
                        }.collect { response->
                            Log.d("main", "openDialog: $response")
                            showMsg("updated successfully...")
                            getBusData()
                        }
                    }
                    dialog.dismiss()
                } else {
                    showMsg("please fill all the fields...")
                }
            }
        }

        dialog.show()
    }
}