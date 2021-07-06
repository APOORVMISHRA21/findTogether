package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findlost.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import Models.Post;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private ArrayList<Post> postList;
    Context context;

    public PostAdapter(ArrayList<Post> postList, Context context){
        this.postList = postList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_post, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mProfilePic, mPostMedia;
        TextView mCreatorName, mCreationDate, mPostCategory, mPostDescription;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mProfilePic = (ImageView) itemView.findViewById(R.id.sample_post_profile_image);
            mPostMedia = (ImageView) itemView.findViewById(R.id.sample_post_media);
            mCreatorName = (TextView) itemView.findViewById(R.id.sample_post_createrName);
            mCreationDate = (TextView) itemView.findViewById(R.id.sample_post_creationTime);
            mPostCategory = (TextView) itemView.findViewById(R.id.sample_post_category);
            mPostDescription = (TextView) itemView.findViewById(R.id.sample_post_description);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Post post = postList.get(position);
        holder.mProfilePic.setImageResource(R.drawable.ic_user);
        //holder.mPostMedia.setImageResource(R.drawable.ic_broken_image);
        Picasso.get().load(post.getMediaUrl()).placeholder(R.drawable.ic_broken_image).into(holder.mPostMedia);
        holder.mCreatorName.setText(post.getCreatorName());
        holder.mCreationDate.setText(post.getCreationDate());
        holder.mPostCategory.setText(post.getCategory());
        holder.mPostDescription.setText(post.getDescription());
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

}
