package paste3.bakersbox;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewAdapter extends BaseAdapter{

    // Declare Variables

    Context mContext;
    LayoutInflater inflater;
    private List<RecipeNames> recipeNamesList = null;
    private ArrayList<RecipeNames> arraylist;


    public ListViewAdapter(Context context, List<RecipeNames> recipeNamesList) {
        mContext = context;
        this.recipeNamesList = recipeNamesList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<RecipeNames>();
        this.arraylist.addAll(recipeNamesList);
    }

    public class ViewHolder {
        TextView name;
    }
    @Override
    public int getCount() {
        return recipeNamesList.size();
    }

    @Override
    public RecipeNames getItem(int position) {
        return recipeNamesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.activity_list_view_items, null);

            // Locate the TextViews in listview_item.xml
            holder.name = (TextView) view.findViewById(R.id.name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText(recipeNamesList.get(position).getRecipeName());
        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        recipeNamesList.clear();
        if (charText.length() == 0) {
            recipeNamesList.addAll(arraylist);
        } else {
            for (RecipeNames ing : arraylist) {
                if (ing.getRecipeName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    recipeNamesList.add(ing);
                }
            }
        }
        notifyDataSetChanged();
    }
}
