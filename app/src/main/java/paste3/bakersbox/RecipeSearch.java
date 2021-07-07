//package paste3.bakersbox;
//
//import androidx.appcompat.app.AppCompatActivity;
//import android.widget.SearchView.OnQueryTextListener;
//import android.os.Bundle;
//import android.widget.ListView;
//import android.widget.SearchView;
//
//import java.util.ArrayList;
//
//
//public class RecipeSearch extends AppCompatActivity implements SearchView.OnQueryTextListener {
//    // Declare Variables
//    ListView list;
//    ListViewAdapter adapter;
//    SearchView editsearch;
//
//    // String[] recipeNameList;
//    // ArrayList<RecipeNames> arraylist = new ArrayList<RecipeNames>();
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.search_view_layout);
//
//
//        // Locate the ListView in listview_main.xml
//        list = (ListView) findViewById(R.id.listview);
//
////        for (int i = 0; i < recipeNameList.length; i++) {
////            RecipeNames recipeNames = new RecipeNames(recipeNameList[i]);
////            // Binds all strings into an array
////            arraylist.add(recipeNames);
////        }
//
//        // Pass results to ListViewAdapter Class
//        adapter = new ListViewAdapter(this, RecipeManager.getRecipeNameList());
//
//        // Binds the Adapter to the ListView
//        list.setAdapter(adapter);
//
//        // Locate the EditText in listview_main.xml
//        editsearch = (SearchView) findViewById(R.id.search);
//        editsearch.setOnQueryTextListener(this);
//    }
//    @Override
//    public boolean onQueryTextSubmit(String query) {
//
//        return false;
//    }
//
//    @Override
//    public boolean onQueryTextChange(String newText) {
//        String text = newText;
//        adapter.filter(text);
//        return false;
//    }
//}