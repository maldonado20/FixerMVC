package sv.udb.fixer.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sv.udb.fixer.databinding.ItemRateBinding
import sv.udb.fixer.model.Rate

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
    }

    override fun getItemCount() = items.size

    fun submit(list: List<Rate>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }
}
