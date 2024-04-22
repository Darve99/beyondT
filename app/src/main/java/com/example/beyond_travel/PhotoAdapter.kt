import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.beyond_travel.Photo
import com.example.beyond_travel.R

class PhotoAdapter : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    private var photosList: List<Photo> = ArrayList()

    inner class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val locationTextView: TextView = itemView.findViewById(R.id.locationTextView)
        val photoImageView: ImageView = itemView.findViewById(R.id.photoImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
        return PhotoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val currentPhoto = photosList[position]
        holder.descriptionTextView.text = currentPhoto.description
        holder.locationTextView.text = "Lat: ${currentPhoto.latitude}, Long: ${currentPhoto.longitude}"

        // Utilizar Glide para cargar la imagen desde la URL en la ImageView
        Glide.with(holder.itemView)
            .load(currentPhoto.imageUrl)
            .into(holder.photoImageView)
    }

    override fun getItemCount(): Int {
        return photosList.size
    }

    fun setData(photos: List<Photo>) {
        this.photosList = photos
        notifyDataSetChanged()
    }
}
