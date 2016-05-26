package com.ppp.esir.projetvelo.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mikepenz.fastadapter.utils.ViewHolderFactory;
import com.mikepenz.materialdrawer.model.BaseDrawerItem;
import com.ppp.esir.projetvelo.R;

import net.steamcrafted.materialiconlib.MaterialIconView;

/**
 * Created by thoma on 25/05/2016.
 */
public class IDrawerItemSearchItinerary extends BaseDrawerItem<IDrawerItemSearchItinerary, IDrawerItemSearchItinerary.ViewHolderEditText> {
    private View.OnClickListener onClickListener;
    private ViewHolderEditText viewHolderEditText;

    public IDrawerItemSearchItinerary(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public String getDepart() {
        return viewHolderEditText.editTextDepart.getText().toString();
    }

    public String getArrivee() {
        return viewHolderEditText.editTextArrivee.getText().toString();
    }

    @Override
    public ViewHolderFactory<IDrawerItemSearchItinerary.ViewHolderEditText> getFactory() {
        return new ItemFactory();
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.i_drawer_item_search_itineraray;
    }

    @Override
    public void bindView(ViewHolderEditText viewHolder) {
        Context ctx = viewHolder.itemView.getContext();
        //bind the basic view parts
        viewHolder.buttonSearch.setOnClickListener(this.onClickListener);
        viewHolder.myLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolderEditText.getEditTextDepart().setText("Ma position");
            }
        });
        this.viewHolderEditText = viewHolder;
        //call the onPostBindView method to trigger post bind view actions (like the listener to modify the item if required)
        onPostBindView(this, viewHolder.itemView);

    }

    public static class ItemFactory implements ViewHolderFactory<IDrawerItemSearchItinerary.ViewHolderEditText> {
        public ViewHolderEditText create(View v) {
            return new ViewHolderEditText(v);
        }
    }

    public static class ViewHolderEditText extends RecyclerView.ViewHolder {
        private EditText editTextDepart, editTextArrivee;
        private Button buttonSearch;
        private MaterialIconView myLocation;

        public ViewHolderEditText(View itemView) {
            super(itemView);
            this.editTextDepart = (EditText) itemView.findViewById(R.id.editDepart);
            this.editTextArrivee = (EditText) itemView.findViewById(R.id.editArrivee);
            this.buttonSearch = (Button) itemView.findViewById(R.id.btnSearch);
            this.myLocation = (MaterialIconView) itemView.findViewById(R.id.myLocation);
        }

        public EditText getEditTextDepart() {
            return editTextDepart;
        }

        public EditText getEditTextArrivee() {
            return editTextArrivee;
        }

        public Button getButtonSearch() {
            return buttonSearch;
        }
    }
}
