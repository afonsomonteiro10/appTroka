package pt.ipsantarem.apptroka.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import pt.ipsantarem.apptroka.R
import pt.ipsantarem.apptroka.model.UserProfile

class MatchAdapter(
    private val matches: List<UserProfile>,
    private val onItemClick: (UserProfile) -> Unit
) : RecyclerView.Adapter<MatchAdapter.MatchViewHolder>() {

    inner class MatchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageProfile: ImageView = itemView.findViewById(R.id.image_match_profile)
        private val textName: TextView = itemView.findViewById(R.id.text_match_name)

        fun bind(profile: UserProfile) {
            if (profile.photoUrl.isNotEmpty()) {
                Glide.with(itemView.context).load(profile.photoUrl).into(imageProfile)
            } else {
                imageProfile.setImageResource(R.drawable.ic_placeholder_profile)
            }
            textName.text = profile.name

            itemView.setOnClickListener { onItemClick(profile) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_match, parent, false)
        return MatchViewHolder(view)
    }

    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        holder.bind(matches[position])
    }

    override fun getItemCount(): Int = matches.size
}
