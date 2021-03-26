package com.hmis_tn.doctor.ui.emr_workflow.chief_complaint.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.ui.emr_workflow.chief_complaint.model.duration.DurationResponseContent
import com.hmis_tn.doctor.ui.emr_workflow.chief_complaint.model.search_complaint.ResponseContent
import com.hmis_tn.doctor.ui.emr_workflow.model.favourite.FavouritesModel


class ChiefComplaintsAdapter(
    private val context: Activity,
    private val favouritesArrayList: ArrayList<FavouritesModel?>
) :
    RecyclerView.Adapter<ChiefComplaintsAdapter.MyViewHolder>() {
    private var onItemClickListener: OnItemClickListener? = null
    private var onDeleteClickListener: OnDeleteClickListener? = null
    private var onSearchInitiatedListener: OnSearchInitiatedListener? = null
    private var durationArrayList: ArrayList<DurationResponseContent?>? = ArrayList()
    var hashmapDuration: HashMap<Int, Int> = HashMap()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.row_chief_complaint, parent, false)
        return MyViewHolder(view)
    }

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val chiefComplaintsModel = favouritesArrayList[position]!!
        holder.serialNumberTextView.text = (position + 1).toString()
        holder.autoCompleteTextView.setText(chiefComplaintsModel.itemAppendString, false)
        holder.durationTextView.text = chiefComplaintsModel.duration
        holder.durationTextView.text = chiefComplaintsModel.duration
        holder.autoCompleteTextView.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                if (s.length > 2) {
                    onSearchInitiatedListener?.onSearchInitiated(
                        s.toString(),
                        holder.autoCompleteTextView,
                        position
                    )
                }
            }
        })

        holder.durationRecyclerView.addOnItemTouchListener(object : OnItemTouchListener {
            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
            }

            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                val action = e.action
                when (action) {
                    MotionEvent.ACTION_MOVE -> rv.parent
                        .requestDisallowInterceptTouchEvent(true)
                }
                return false
            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
            }
        })

        if (position % 2 == 0) {
            holder.mainLinearLayout.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.alternateRow
                )
            )
        } else {
            holder.mainLinearLayout.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.white
                )
            )
        }

        holder.minusImageView.setOnClickListener {
            holder.durationTextView.text =
                (holder.durationTextView.text.toString().toInt() - 1).toString()
            chiefComplaintsModel.duration = holder.durationTextView.text.toString()
            if (holder.durationTextView.text.toString().toInt() == 0) {
                holder.minusImageView.alpha = 0.5f
                holder.minusImageView.isEnabled = false
            }
        }

        holder.plusImageView.setOnClickListener {
            holder.minusImageView.alpha = 1f
            holder.minusImageView.isEnabled = true
            holder.durationTextView.text =
                (holder.durationTextView.text.toString().toInt() + 1).toString()
            chiefComplaintsModel.duration = holder.durationTextView.text.toString()
        }

        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(chiefComplaintsModel, position)
        }

        holder.deleteImageView.setOnClickListener {

            onDeleteClickListener?.onDeleteClick(chiefComplaintsModel, position)

            hashmapDuration.remove(chiefComplaintsModel.chief_complaint_id)

        }

        val gridLayoutManager = GridLayoutManager(
            context, 1,
            LinearLayoutManager.HORIZONTAL, false
        )
        holder.durationRecyclerView.layoutManager = gridLayoutManager
        if (chiefComplaintsModel.durationPeriodId == 0) {
            if (durationArrayList?.isNotEmpty()!!) {
                chiefComplaintsModel.durationPeriodId =
                    durationArrayList?.get(0)?.duration_period_id
            }
        }
        val durationAdapter = DurationAdapter(context, durationArrayList)

        holder.durationRecyclerView.adapter = durationAdapter

        try {
            if (hashmapDuration.containsKey(chiefComplaintsModel.chief_complaint_id!!)) {
                durationAdapter.setFocus()
            }
        } catch (e: Exception) {

        }
        Log.i("hashmap", "" + hashmapDuration)
        if (hashmapDuration.get(chiefComplaintsModel.chief_complaint_id!!) != null) {
            val pos = hashmapDuration.get(chiefComplaintsModel.chief_complaint_id!!)
            durationAdapter.updateSelectStatus(pos!!)
        } else {
            hashmapDuration.put(chiefComplaintsModel.chief_complaint_id!!, 1)
            //   durationAdapter.updateSelectStatus(0)
        }

        durationAdapter.setOnItemClickListener(object : DurationAdapter.OnItemClickListener {
            override fun onItemClick(durationID: Int, selectedPosition: Int) {
                hashmapDuration.put(chiefComplaintsModel.chief_complaint_id!!, durationID)
                chiefComplaintsModel.durationPeriodId = durationID
                durationAdapter.updateSelectStatus((durationID))
            }
        })


        if (position == favouritesArrayList.size - 1) {
            holder.deleteImageView.alpha = 0.2f
            holder.deleteImageView.isEnabled = false
            holder.autoCompleteTextView.isFocusable = true
            holder.autoCompleteTextView.isFocusableInTouchMode = true
            holder.autoCompleteTextView.requestFocus()


        } else {
            holder.deleteImageView.alpha = 1f
            holder.deleteImageView.isEnabled = true

            holder.autoCompleteTextView.isFocusable = false
            holder.autoCompleteTextView.isFocusableInTouchMode = false
        }
    }

    override fun getItemCount(): Int {
        return favouritesArrayList.size
    }

    inner class MyViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val serialNumberTextView: TextView =
            itemView.findViewById(R.id.serialNumberTextView)
        internal val deleteImageView: ImageView = itemView.findViewById(R.id.deleteImageView)
        internal val autoCompleteTextView: AppCompatAutoCompleteTextView =
            itemView.findViewById(R.id.autoCompleteTextView)
        internal val mainLinearLayout: LinearLayout = itemView.findViewById(R.id.mainLinearLayout)
        internal val minusImageView: ImageView = itemView.findViewById(R.id.minusImageView)
        internal val plusImageView: ImageView = itemView.findViewById(R.id.plusImageView)
        internal val durationTextView: TextView = itemView.findViewById(R.id.durationTextView)
        internal val durationRecyclerView: RecyclerView =
            itemView.findViewById(R.id.durationRecyclerView)
    }

    interface OnItemClickListener {
        fun onItemClick(
            favouritesModel: FavouritesModel?,
            position: Int
        )
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnDeleteClickListener {
        fun onDeleteClick(
            favouritesModel: FavouritesModel?,
            position: Int
        )
    }

    fun setOnDeleteClickListener(onDeleteClickListener: OnDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener
    }

    interface OnSearchInitiatedListener {
        fun onSearchInitiated(
            query: String,
            view: AppCompatAutoCompleteTextView,
            position: Int
        )
    }

    fun setOnSearchInitiatedListener(onSearchInitiatedListener: OnSearchInitiatedListener) {
        this.onSearchInitiatedListener = onSearchInitiatedListener
    }

    fun deleteRow(
        favouritesModel: FavouritesModel?,
        position1: Int
    ) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(context.currentFocus?.windowToken, 0)
        this.favouritesArrayList.removeAt(position1)
        notifyDataSetChanged()
    }

    fun deleteRowFromFavourites(
        favouritesModel: FavouritesModel?,
        position1: Int
    ) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(context.currentFocus?.windowToken, 0)
        this.favouritesArrayList.remove(favouritesModel)
        notifyDataSetChanged()
    }

    fun addFavouritesInRow(
        favouritesModel: FavouritesModel?
    ) {
        val check =
            favouritesArrayList.any { it!!.chief_complaint_id == favouritesModel?.chief_complaint_id }
        if (!check) {
            favouritesArrayList.removeAt(favouritesArrayList.size - 1)
            favouritesModel?.itemAppendString = favouritesModel?.chief_complaint_name
            favouritesArrayList.add(favouritesModel)
            favouritesArrayList.add(FavouritesModel())
            notifyDataSetChanged()
        } else {
            notifyDataSetChanged()
            Toast.makeText(context, "Already Item available in the list", Toast.LENGTH_LONG)?.show()
        }
    }

    fun addRow(
        favouritesModel: FavouritesModel?
    ) {
        val check =
            favouritesArrayList.any { it!!.chief_complaint_id == favouritesModel?.chief_complaint_id }
        if (!check) {
            favouritesArrayList.add(favouritesModel)
            notifyDataSetChanged()
        } else {
            notifyDataSetChanged()
            //Toast.makeText(context,"Already Item available in the list", Toast.LENGTH_LONG)?.show()
        }
    }

    fun clearall() {
        favouritesArrayList.clear()
        notifyDataSetChanged()
    }

    fun setDuration(durationArrayList_: ArrayList<DurationResponseContent?>) {
        this.durationArrayList = durationArrayList_
        notifyDataSetChanged()
    }

    fun getall(): ArrayList<FavouritesModel?> {
        return this.favouritesArrayList
    }

    fun setAdapter(
        dropdownReferenceView: AppCompatAutoCompleteTextView,
        responseContents: ArrayList<ResponseContent>,
        selectedSearchPosition: Int
    ) {
        val responseContentAdapter = ChiefComplaintSearchResultAdapter(
            context,
            R.layout.row_chief_complaint_search_result,
            responseContents
        )
        dropdownReferenceView.threshold = 1
        dropdownReferenceView.setAdapter(responseContentAdapter)
        dropdownReferenceView.setOnItemClickListener { parent, _, position, id ->
            val selectedPoi = parent.adapter.getItem(position) as ResponseContent?
            val check = favouritesArrayList.any { it!!.chief_complaint_id == selectedPoi?.uuid }
            if (!check) {
                dropdownReferenceView.setText(selectedPoi?.name)
                favouritesArrayList[selectedSearchPosition]?.chief_complaint_name =
                    selectedPoi?.name
                favouritesArrayList[selectedSearchPosition]?.itemAppendString = selectedPoi?.name
                favouritesArrayList[selectedSearchPosition]?.test_master_name = selectedPoi?.name
                favouritesArrayList[selectedSearchPosition]?.chief_complaint_id = selectedPoi?.uuid
                favouritesArrayList[selectedSearchPosition]?.chief_complaint_code =
                    selectedPoi?.code.toString()
                addRow(FavouritesModel())
                notifyDataSetChanged()
            } else {
                notifyDataSetChanged()
                Toast.makeText(context, "Already Item available in the list", Toast.LENGTH_LONG)
                    ?.show()
            }
        }
    }

    fun deleteRowFromFav(
        chiefcomplainID: Int,
        position1: Int
    ) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(context.currentFocus?.windowToken, 0)
        val OriginalItemCount = itemCount
        if (favouritesArrayList.size > 0) {
            favouritesArrayList.removeAt(favouritesArrayList.size - 1)
        }

        for (i in favouritesArrayList.indices) {
            if (favouritesArrayList.get(i)?.chief_complaint_id?.equals(chiefcomplainID)!!) {
                this.favouritesArrayList.removeAt(i)
                notifyItemRemoved(i)
                break
            }
        }
        notifyDataSetChanged()
        addRow(FavouritesModel())
    }
}
