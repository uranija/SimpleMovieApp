package com.jolita.simplemovieapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.jolita.simplemovieapp.DetailActivity;
import com.jolita.simplemovieapp.R;
import com.jolita.simplemovieapp.model.Movie;
import com.squareup.picasso.Picasso;
import java.util.List;
import androidx.recyclerview.widget.RecyclerView;

public class
MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movieList;

    private Context mContext;

    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the list is the data we want
     * to populate into the lists.
     *
     * @param mContext  The current context. Used to inflate the layout file.
     * @param movieList A List of Movie objects to display in a list
     */

    public MovieAdapter(Context mContext, List<Movie> movieList) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.

        this.mContext = mContext;
        this.movieList = movieList;


    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  *                  can use this viewType integer to provide a different layout.
     * @return A new MovieViewHolder that holds the View for each list item
     */


    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie_card, viewGroup, false);

        return new MovieViewHolder(view);
    }


    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the correct
     * indices in the list for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param viewHolder The ViewHolder which should be updated to represent the contents of the
     *                   item at the given position in the data set.
     * @param position   The position of the item within the adapter's data set.
     */

    @Override
    public void onBindViewHolder(final MovieViewHolder viewHolder, int position) {


        Picasso.get()
                .load("https://image.tmdb.org/t/p/w500" + movieList.get(position).getPosterPath())
                .fit()
                .centerCrop()
                .placeholder(R.drawable.load)
                .into(viewHolder.thumbnail);
    }


    public void setMovies(List<Movie> movie) {
        movieList = movie;
        notifyDataSetChanged();
    }


    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available
     */
    @Override
    public int getItemCount() {
        return movieList.size();

    }

    /**
     * Cache of the children views for a list item.
     */


    public class MovieViewHolder extends RecyclerView.ViewHolder {

        public ImageView thumbnail;


        public MovieViewHolder(View itemView) {
            super(itemView);

            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);

            /**
             * Constructor for our ViewHolder. Within this constructor, we get a reference to our
             * TextViews and set an onClickListener to listen for clicks. Those will be handled in the
             * onClick method below.
             * @param itemView The View that you inflated in
             *                 {@link GreenAdapter#onCreateViewHolder(ViewGroup, int)}
             */


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {


                        Movie clickedMovieCard = movieList.get(pos);
                        Intent intent = new Intent(mContext, DetailActivity.class);
                        intent.putExtra("Movie", clickedMovieCard);



                        /* This is the class that we want to start (and open) when the button is clicked. */
                        //Intent intent=new Intent(mContext, DetailActivity.class);
                        /*
                         * We use the putExtra method of the Intent class to pass some extra stuff to the
                         * Activity that we are starting. Generally, this data is quite simple, such as
                         * a String or a number. However, there are ways to pass more complex objects.
                         */

                        // intent.putExtra("original_title", movieList.get(pos).getOriginalTitle());
                        //  intent.putExtra("poster_path", movieList.get(pos).getPosterPath());
                        //  intent.putExtra("overview", movieList.get(pos).getOverview());
                        //  intent.putExtra("vote_average", Double.toString(movieList.get(pos).getVoteAverage()));
                        // intent.putExtra("release_date", movieList.get(pos).getReleaseDate());
                        //  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        /*
                         * Once the Intent has been created, we can use Activity's method, "startActivity"
                         * to start the ChildActivity.
                         */
                        //mContext.startActivity(intent);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);


                    }


                }
            });


        }


    }
}
