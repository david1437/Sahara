package sahara.sahara;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    public ArrayList<Product> mProducts;
    private LayoutInflater mLayoutInflater;
    private ItemClickListener mClickListener;

    public ProductAdapter(Context context, ArrayList<Product> p) {
        mLayoutInflater = LayoutInflater.from(context);
        mProducts = p;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView category;
        public TextView title;
        public TextView price;
        public TextView quantity;
        public ViewHolder(View v) {
            super(v);
            category = (TextView) v.findViewById(R.id.category);
            title = (TextView) v.findViewById(R.id.product);
            price = (TextView) v.findViewById(R.id.price);
            quantity = (TextView) v.findViewById(R.id.quantity);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) mClickListener.onItemClick(v, getAdapterPosition());
        }

    }

    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = mLayoutInflater.inflate(R.layout.product_row, parent, false);
        ProductAdapter.ViewHolder vh = new ProductAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ProductAdapter.ViewHolder holder, int position) {
        holder.category.setText(mProducts.get(position).category);
        holder.price.setText("$" + Float.toString(mProducts.get(position).price));
        holder.quantity.setText("x" + mProducts.get(position).quantity);
        holder.title.setText(mProducts.get(position).title);
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    Product getItem(int position) {
        return mProducts.get(position);
    }
}