package sv.udb.fixer.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import sv.udb.fixer.R
import sv.udb.fixer.databinding.ItemRateBinding
import sv.udb.fixer.model.Rate
import sv.udb.fixer.util.Flags

class RatesAdapter(
    private val items: MutableList<Rate> = mutableListOf()
) : RecyclerView.Adapter<RatesAdapter.VH>() {

    inner class VH(val binding: ItemRateBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inf = LayoutInflater.from(parent.context)
        val binding = ItemRateBinding.inflate(inf, parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.binding.tvCode.text = item.code
        holder.binding.tvValue.text = item.value.toString()

        val url = Flags.urlFor(item.code)
        if (url != null)
            Glide.with(holder.itemView).load(url).placeholder(R.drawable.ic_money).into(holder.binding.imgFlag)
        else
            holder.binding.imgFlag.setImageResource(R.drawable.ic_money)
    }

    override fun getItemCount() = items.size

    fun submit(list: List<Rate>) {
        items.clear(); items.addAll(list); notifyDataSetChanged()
    }
}
