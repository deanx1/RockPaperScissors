package com.androidcourse.shoppinglistkotlin.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.androidcourse.shoppinglistkotlin.model.Product
import com.androidcourse.shoppinglistkotlin.R
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.item_product.view.*

class ProductAdapter(private val products: List<Product>) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        )
    }

    override fun getItemCount(): Int = products.size

//    , you chose paper.
    private fun setImage(play :String) :Int {
    if (play.contains("rock")) {
        return 1
    }
    if (play.contains("paper")) {
        return 2
    }
        return 3
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(products[position])

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(product: Product) {

            var imgRock = R.drawable.rock
            var imgPaper = R.drawable.paper
            var imgScissors = R.drawable.scissors

            val Strings = arrayOf(imgRock, imgPaper, imgScissors)

//            itemView.tvProduct.text = product.whowon + product.computerplay + product.yourplay
//            itemView.tvProduct.text = product.whowon + product.computerplay + product.yourplay
            itemView.tvWhoWon.text = product.whowon
            itemView.tvDate.text = product.date

            var computerPlay = setImage(product.computerplay)
            var yourPlay = setImage(product.yourplay)
//            itemView.ivComputerHistory.setImageResource(R.drawable.rock)

//            itemView.ivComputerHistory.setImageResource(Strings[computerPlay])
//            itemView.ivYouHistory.setImageResource(Strings[yourPlay])

            if (computerPlay == 1) {
                itemView.ivComputerHistory.setImageResource(R.drawable.rock)
            } else if (computerPlay == 2) {
                itemView.ivComputerHistory.setImageResource(R.drawable.paper)
            } else {
                itemView.ivComputerHistory.setImageResource(R.drawable.scissors)
            }

            if (yourPlay == 1) {
                itemView.ivYouHistory.setImageResource(R.drawable.rock)
            } else if (computerPlay == 2) {
                itemView.ivYouHistory.setImageResource(R.drawable.paper)
            } else {
                itemView.ivYouHistory.setImageResource(R.drawable.scissors)
            }

        }
    }
}