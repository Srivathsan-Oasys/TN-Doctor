package com.oasys.digihealth.doctor.ui.configuration.view

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.oasys.digihealth.doctor.ui.configuration.view.ConfigFavRecyclerAdapter

class ItemMoveCallback(private val mAdapter: ConfigFavRecyclerAdapter?) :
    ItemTouchHelper.Callback() {

    override fun isLongPressDragEnabled(): Boolean {
        return false
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return false
    }


    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {

    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, 0)
    }

    override fun onMove(
        recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        mAdapter!!.onRowMoved(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSelectedChanged(
        viewHolder: RecyclerView.ViewHolder?,
        actionState: Int
    ) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder is ConfigFavRecyclerAdapter.MyViewHolder) {
                val myViewHolder = viewHolder as ConfigFavRecyclerAdapter.MyViewHolder?
                mAdapter!!.onRowSelected(myViewHolder!!)
            }
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ) {
        super.clearView(recyclerView, viewHolder)

        if (viewHolder is ConfigFavRecyclerAdapter.MyViewHolder) {
            mAdapter!!.onRowClear(viewHolder)
        }
    }

    interface ItemTouchHelperContract {
        fun onRowMoved(fromPosition: Int, toPosition: Int)
        fun onRowSelected(myViewHolder: ConfigFavRecyclerAdapter.MyViewHolder)
        fun onRowClear(myViewHolder: ConfigFavRecyclerAdapter.MyViewHolder)
    }
}
