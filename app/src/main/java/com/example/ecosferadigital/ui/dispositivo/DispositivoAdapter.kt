import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecosferadigital.R
import com.example.ecosferadigital.models.Dispositivo

class DispositivoAdapter(
    private val dispositivos: List<Dispositivo>,
    private val onExcluirClick: (Dispositivo) -> Unit
) : RecyclerView.Adapter<DispositivoAdapter.DispositivoViewHolder>() {

    inner class DispositivoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTipoDispositivo: TextView = view.findViewById(R.id.tvTipoDispositivo)
        val tvDescricao: TextView = view.findViewById(R.id.tvDescricao)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
        val btnExcluir: TextView = view.findViewById(R.id.btnExcluirDispositivo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DispositivoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dispositivo, parent, false)
        return DispositivoViewHolder(view)
    }

    override fun onBindViewHolder(holder: DispositivoViewHolder, position: Int) {
        val dispositivo = dispositivos[position]
        holder.tvTipoDispositivo.text = dispositivo.tipoDispositivo
        holder.tvDescricao.text = dispositivo.descricao
        holder.tvStatus.text = dispositivo.status
        holder.btnExcluir.setOnClickListener { onExcluirClick(dispositivo) }
    }

    override fun getItemCount(): Int = dispositivos.size
}
