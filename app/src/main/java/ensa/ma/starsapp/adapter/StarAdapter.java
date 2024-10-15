package ensa.ma.starsapp.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import ensa.ma.starsapp.R;
import ensa.ma.starsapp.beans.Star;
import ensa.ma.starsapp.service.StarService;

public class StarAdapter extends RecyclerView.Adapter<StarAdapter.StarViewHolder> implements Filterable {
    private static final String TAG = "StarAdapter";
    private List<Star> stars; // Liste originale
    private List<Star> starsFilter; // Liste filtrée
    private Context context;
    private NewFilter mFilter;

    public StarAdapter(Context context, List<Star> stars) {
        this.context = context;
        this.stars = stars;
        this.starsFilter = new ArrayList<>(stars); // Initialise avec tous les éléments
        this.mFilter = new NewFilter();
    }

    @NonNull

    @Override
    public StarViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(this.context).inflate(R.layout.star_item, viewGroup, false);
        final StarViewHolder holder = new StarViewHolder(v);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Créer la vue pour l'édition
                View popup = LayoutInflater.from(context).inflate(R.layout.star_edit_item, null, false);

                final ImageView img = popup.findViewById(R.id.img);
                final RatingBar bar = popup.findViewById(R.id.ratingBar);
                final TextView idss = popup.findViewById(R.id.idss);

                // Récupérer les informations de l'item cliqué
                Star star = starsFilter.get(holder.getAdapterPosition());
                Glide.with(context)
                        .load(star.getImg())
                        .into(img);

                bar.setRating(star.getStar());
                idss.setText(String.valueOf(star.getId()));

                // Créer et afficher la boîte de dialogue
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setTitle("Notez :")
                        .setMessage("Donner une note entre 1 et 5 :")
                        .setView(popup)
                        .setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                float newRating = bar.getRating();
                                int starId = Integer.parseInt(idss.getText().toString());

                                // Mettre à jour la note de l'étoile
                                Star star = StarService.getInstance().findById(starId);
                                star.setStar(newRating);
                                StarService.getInstance().update(star);

                                // Notifiez l'adapter pour rafraîchir l'affichage de l'item modifié
                                notifyItemChanged(holder.getAdapterPosition());
                            }
                        })
                        .setNegativeButton("Annuler", null)
                        .create();
                dialog.show();
            }
        });

        return holder; // N'oubliez pas de retourner le holder créé
    }

    @Override
    public void onBindViewHolder(@NonNull StarViewHolder starViewHolder, int i) {
        Log.d(TAG, "onBindView call! Position: " + i);
        Star star = starsFilter.get(i); // Utilisez starsFilter pour afficher

        Glide.with(context)
                .asBitmap()
                .load(star.getImg())
                .apply(new RequestOptions().override(100, 100))
                .into(starViewHolder.img);

        starViewHolder.name.setText(star.getName().toUpperCase());
        starViewHolder.stars.setRating(star.getStar());
        starViewHolder.idss.setText(String.valueOf(star.getId()));
    }

    @Override
    public int getItemCount() {
        return starsFilter.size(); // Retourne la taille de la liste filtrée
    }

    @Override
    public Filter getFilter() {
        return mFilter; // Retourne le filtre
    }

    public class StarViewHolder extends RecyclerView.ViewHolder {
        TextView idss;
        ImageView img;
        TextView name;
        RatingBar stars;
        RelativeLayout parent;

        public StarViewHolder(@NonNull View itemView) {
            super(itemView);
            idss = itemView.findViewById(R.id.ids);
            img = itemView.findViewById(R.id.img);
            name = itemView.findViewById(R.id.name);
            stars = itemView.findViewById(R.id.stars);
            parent = itemView.findViewById(R.id.parent);
        }
    }

    private class NewFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Star> filteredList = new ArrayList<>();
            final FilterResults results = new FilterResults();

            // Vérifiez si la chaîne de recherche est vide
            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(stars); // Ajouter tous les éléments si aucune recherche
            } else {
                final String filterPattern = charSequence.toString().toLowerCase().trim(); // Convertir en minuscules
                for (Star star : stars) {
                    // Vérifiez si le nom contient le texte de recherche
                    if (star.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(star);
                    }
                }
            }

            results.values = filteredList; // Stockez les résultats filtrés
            results.count = filteredList.size(); // Mettez à jour le compte
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            starsFilter.clear(); // Effacez la liste actuelle
            starsFilter.addAll((List<Star>) filterResults.values); // Mettez à jour avec les résultats filtrés
            notifyDataSetChanged(); // Notifiez l'adapter pour rafraîchir l'affichage
        }
    }
}
