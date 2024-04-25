package com.test.practical.ui

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.test.practical.MainRepository
import com.test.practical.MainViewModel
import com.test.practical.MyViewModelFactory
import com.test.practical.R
import com.test.practical.apiservices.RetrofitService
import com.test.practical.databinding.ActivityMainBinding
import com.test.practical.model.Items
import com.test.practical.ui.adapter.ItemAdapter
import com.test.practical.worker.MyWork
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    private lateinit var searchView: SearchView
    lateinit var viewModel: MainViewModel
    private val adapter = ItemAdapter()
    lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "Trending"
        var toolbar = binding.toolbar
        toolbar.title = title
        toolbar.setBackgroundColor(ContextCompat.getColor(this,R.color.white))
        toolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.black))

        val retrofitService = RetrofitService.getInstance()
        val mainRepository = MainRepository(retrofitService)

        binding.recyclerview.adapter = adapter

        viewModel =
            ViewModelProvider(this, MyViewModelFactory(mainRepository))[MainViewModel::class.java]

        viewModel.itemList.observe(this) {
            adapter.setItems(it.items)

            binding.progressDialog.visibility = View.GONE
            binding.noInternet.visibility = View.GONE
            binding.retry.visibility = View.GONE
            binding.wentWrong.visibility = View.GONE
            binding.listLayout.visibility = View.VISIBLE
            binding.progressDialog.visibility = View.GONE
            if(binding.swipe.isRefreshing){
                binding.swipe.isRefreshing = false
            }
        }

        viewModel.errorMessage.observe(this) {
            if(binding.swipe.isRefreshing){
                binding.swipe.isRefreshing = false
            }
            binding.noInternet.visibility = View.GONE
            binding.retry.visibility = View.VISIBLE
            binding.wentWrong.visibility = View.VISIBLE
            binding.listLayout.visibility = View.GONE
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.loading.observe(this, Observer {
            if (it) {
                binding.progressDialog.visibility = View.VISIBLE
            } else {
                binding.progressDialog.visibility = View.GONE
            }
        })

        viewModel.repoItem.getData()?.observe(
            this,
            Observer<List<Items>> {
                if (it != null) {
                    if (it.isNotEmpty()) {
                        adapter.setItems(it)
                        binding.progressDialog.visibility = View.GONE
                        binding.noInternet.visibility = View.GONE
                        binding.retry.visibility = View.GONE
                        binding.wentWrong.visibility = View.GONE
                        binding.listLayout.visibility = View.VISIBLE
                    } else {
                        apiCall()
                    }
                }
            })

        binding.swipe.setOnRefreshListener(OnRefreshListener {
            apiCall()
        })

        val myWorkRequest = PeriodicWorkRequest.Builder(MyWork::class.java, 120, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(this).enqueue(myWorkRequest)

        binding.retry.setOnClickListener(View.OnClickListener {
            apiCall()
        })
    }

    private fun apiCall() {
        binding.progressDialog.visibility = View.VISIBLE
        if (viewModel.isInternetAvailable(this@MainActivity)) {
            viewModel.getAllItems()
        } else {
            if(binding.swipe.isRefreshing){
                binding.swipe.isRefreshing = false
            }
            binding.noInternet.visibility = View.VISIBLE
            binding.retry.visibility = View.VISIBLE
            binding.wentWrong.visibility = View.GONE
            binding.progressDialog.visibility = View.GONE
            binding.listLayout.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.tools, menu)
        val searchItem: MenuItem = menu.findItem(R.id.action_search)
        /*if (searchItem != null) {
            searchView = searchItem.actionView as SearchView
            searchView.setOnCloseListener { true }

            val searchPlate =
                searchView.findViewById(androidx.appcompat.R.id.search_src_text) as EditText
            searchPlate.hint = "Search"
            val searchPlateView: View =
                searchView.findViewById(androidx.appcompat.R.id.search_plate)
            searchPlateView.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    android.R.color.transparent
                )
            )

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
// do your logic here                Toast.makeText(applicationContext, query, Toast.LENGTH_SHORT).show()
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })

            val searchManager =
                getSystemService(Context.SEARCH_SERVICE) as SearchManager
            searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }*/
        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {

        if (searchView != null) {
            if (!searchView.isIconified) {
                searchView.onActionViewCollapsed();
            } else {
                super.onBackPressed();
            }
        }
    }
}