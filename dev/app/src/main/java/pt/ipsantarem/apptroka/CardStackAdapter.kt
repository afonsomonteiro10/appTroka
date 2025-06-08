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

class CardStackAdapter(
    private val profiles: List<UserProfile>
) : RecyclerView.Adapter<CardStackAdapter.CardViewHolder>() {

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageProfile: ImageView = itemView.findViewById(R.id.image_profile)
        val textName: TextView = itemView.findViewById(R.id.text_name)
        val textSkillOffer: TextView = itemView.findViewById(R.id.text_skill_offer)
        val textSkillWant: TextView = itemView.findViewById(R.id.text_skill_want)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_swipe_card, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val profile = profiles[position]

        // Carregar imagem de URL usando Glide (ou Picasso)
        if (profile.photoUrl.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(profile.photoUrl)
                .into(holder.imageProfile)
        } else {
            // se não tiver foto, usar placeholder
            holder.imageProfile.setImageResource(R.drawable.ic_placeholder_profile)
        }

        holder.textName.text = profile.name
        holder.textSkillOffer.text = "Ofereço: ${profile.skillOffer}"
        holder.textSkillWant.text = "Quero: ${profile.skillWant}"
    }

    override fun getItemCount(): Int = profiles.size
}
