package com.nukki.gifted;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.koushikdutta.ion.Ion;
import java.util.ArrayList;
import static com.nukki.gifted.R.drawable.app_icon_final;


/**
 * A simple {@link Fragment} subclass.
 */
public class PageFragment extends Fragment {

    ImageView gif;
    public static final String prefs_name = "MyPreferencesFile";

    public PageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.page_fragment_layout, container,false);
        Bundle bundle = getArguments();
        int index = bundle.getInt("count")  ;
        ArrayList<String> links = bundle.getStringArrayList("links");

        gif = (ImageView) view.findViewById(R.id.gif);

        Ion.with(gif)
                .placeholder(app_icon_final)
                .error(R.drawable.dead)
                .load(links.get(index));

        SharedPreferences settings = getActivity().getSharedPreferences(prefs_name,0);
        SharedPreferences.Editor editor = settings.edit();

        if(index == 0 ) {
            editor.putString("current_link", links.get(0));
        } else if(index == 1 ) {
            editor.putString("current_link", links.get(1));
        } else if (index < 19){
            editor.putString("current_link", links.get(index-1));
        } else {
            editor.putString("current_link", links.get(index));
        }
        editor.commit();

        return view;
    }

}
