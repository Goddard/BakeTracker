package com.goddardlabs.baketracker.Adapters;

import android.view.View;

public interface OnItemClickListener<MODEL> {
        void onClick(MODEL model, View view);
}
