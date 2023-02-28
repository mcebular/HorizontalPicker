package com.github.mcebular.horizontalpicker

import android.view.View

interface OnItemClickListener {
    fun onClickView(v: View?, adapterPosition: Int)
}