package com.androidcourse.shoppinglistkotlin.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidcourse.shoppinglistkotlin.model.Product
import com.androidcourse.shoppinglistkotlin.database.ProductRepository
import com.androidcourse.shoppinglistkotlin.R

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private val shoppingList = arrayListOf<Product>()
    private val productAdapter = ProductAdapter(shoppingList)
    private lateinit var productRepository: ProductRepository
    private val mainScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Rock, Paper, Scissors"

        productRepository = ProductRepository(this)
        initViews()
    }

    private fun initViews() {
//        rvShoppingList.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
//        rvShoppingList.adapter = productAdapter
//        rvShoppingList.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
//        createItemTouchHelper().attachToRecyclerView(rvShoppingList)
//        getShoppingListFromDatabase()

        btnGoHistory.setOnClickListener { onHistoryClick() }

        ibRock.setOnClickListener { chooseRock() }

        ibPaper.setOnClickListener { choosePaper() }

        ibScissors.setOnClickListener { chooseScissors() }

        fab.setOnClickListener { addProduct() }
    }

    private fun chooseRock() {
        val computerOutcome = computerChoose()
        ivYou.setImageResource(R.drawable.rock)
        if (computerOutcome == 1) {
            //draw
            ivComputer.setImageResource(R.drawable.rock)
            draw()
        } else if (computerOutcome == 2) {
            //lose
            ivComputer.setImageResource(R.drawable.paper)
            lose()
        } else  {
            //win
            ivComputer.setImageResource(R.drawable.scissors)
            win()
        }
    }

    private fun choosePaper() {
        val computerOutcome = computerChoose()
        ivYou.setImageResource(R.drawable.paper)
        if (computerOutcome == 1) {
            //win
            ivComputer.setImageResource(R.drawable.rock)
            win()
        } else if (computerOutcome == 2) {
            //draw
            ivComputer.setImageResource(R.drawable.paper)
            draw()
        } else  {
            //lose
            ivComputer.setImageResource(R.drawable.scissors)
            lose()
        }
    }

    private fun chooseScissors() {
        val computerOutcome = computerChoose()
        ivYou.setImageResource(R.drawable.scissors)
        if (computerOutcome == 1) {
            //lose
            ivComputer.setImageResource(R.drawable.rock)
            lose()
        } else if (computerOutcome == 2) {
            //win
            ivComputer.setImageResource(R.drawable.paper)
            win()
        } else  {
            //scissors
            ivComputer.setImageResource(R.drawable.scissors)
            draw()
        }
    }

    private fun win () {
        tvOutcome.text = "You win!"
        ivComputer
    }

    private fun draw () {
        tvOutcome.text = "Draw!"
    }

    private fun lose () {
        tvOutcome.text = "You lose!"
    }

    // 1 is rock, 2 is paper, 3 is scissors
    private fun computerChoose() : Int {
        val randomInteger = (1..3).shuffled().first()
        return randomInteger
    }

    private fun addProduct() {
        if (validateFields()) {
            mainScope.launch {
                val product = Product(
                    name = etProduct.text.toString(),
                    quantity = etQuantity.text.toString().toInt()
                )

                withContext(Dispatchers.IO) {
                    productRepository.insertProduct(product)
                }

                getShoppingListFromDatabase()
            }
        }
    }

    private fun onHistoryClick() {
//        val profile = Profile(
//            etFirstName.text.toString(),
//            etLastName.text.toString(),
//            etProfileDescription.text.toString(),
//            profileImageUri
//        )

        val profileActivityIntent = Intent(this, History::class.java)
//        val profileActivityIntent = Intent(this, MainActivity::class.java)
//        profileActivityIntent.putExtra(ProfileActivity.PROFILE_EXTRA, profile)
        startActivity(profileActivityIntent)
    }

    private fun getShoppingListFromDatabase() {
        mainScope.launch {
            val shoppingList = withContext(Dispatchers.IO) {
                productRepository.getAllProducts()
            }
            this@MainActivity.shoppingList.clear()
            this@MainActivity.shoppingList.addAll(shoppingList)
            this@MainActivity.productAdapter.notifyDataSetChanged()
        }
    }

    private fun validateFields(): Boolean {
        return if (etProduct.text.toString().isNotBlank() && etQuantity.text.toString().isNotBlank()) {
            true
        } else {
            Toast.makeText(this, "Please fill in the fields", Toast.LENGTH_SHORT).show()
            false
        }
    }

    /**
     * Create a touch helper to recognize when a user swipes an item from a recycler view.
     * An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
     * and uses callbacks to signal when a user is performing these actions.
     */
    private fun createItemTouchHelper(): ItemTouchHelper {

        // Callback which is used to create the ItemTouch helper. Only enables left swipe.
        // Use ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) to also enable right swipe.
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            // Enables or Disables the ability to move items up and down.
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            // Callback triggered when a user swiped an item.
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val productToDelete = shoppingList[position]
                mainScope.launch {
                    withContext(Dispatchers.IO) {
                        productRepository.deleteProduct(productToDelete)
                    }
                    getShoppingListFromDatabase()
                }
            }
        }
        return ItemTouchHelper(callback)
    }

    private fun deleteShoppingList() {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                productRepository.deleteAllProducts()
            }
            getShoppingListFromDatabase()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        return when (item.itemId) {
//            R.id.action_delete_shopping_list -> {
//                deleteShoppingList()
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }
}
