package com.putri.widya.devapp.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.putri.widya.devapp.R
import com.putri.widya.devapp.databinding.DevbyteItemBinding
import com.putri.widya.devapp.databinding.FragmentDevByteBinding
import com.putri.widya.devapp.domain.DevByteVideo
import com.putri.widya.devapp.viewmodels.DevByteViewModel

// untuk menampilkan daftar DevBytes di layar
class DevByteFragment : Fragment() {

    // digunakan untuk mengakses viewModel setelah parameter onActivity terbuat.
    private val viewModel: DevByteViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(this, DevByteViewModel.Factory(activity.application))
                .get(DevByteViewModel::class.java)
    }

    // Adaptor RecyclerView untuk mengonversi daftar Video ke cardView
    private var viewModelAdapter: DevByteAdapter? = null

    // Dipanggil segera setelah onCreateView() dikembalikan.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.playlist.observe(viewLifecycleOwner, Observer<List<DevByteVideo>> { videos ->
            videos?.apply {
                viewModelAdapter?.videos = videos
            }
        })
    }

    //  Dipanggil agar fragmen membuat instance tampilan antarmuka penggunanya.
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentDevByteBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_dev_byte,
                container,
                false)

        // Atur lifecycleOwner agar DataBinding dapat mengamati LiveData
        binding.setLifecycleOwner(viewLifecycleOwner)

        binding.viewModel = viewModel

        viewModelAdapter = DevByteAdapter(VideoClick {

            // Saat video diklik, blok atau lambda ini akan dipanggil oleh DevByteAdapter
            val packageManager = context?.packageManager ?: return@VideoClick

            // untuk mengenerate intent ke aplikasi youtube.
            var intent = Intent(Intent.ACTION_VIEW, it.launchUri)
            if(intent.resolveActivity(packageManager) == null) {
                // aplikasi YouTube tidak ditemukan, gunakan url web
                intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.url))
            }

            startActivity(intent)
        })

        binding.root.findViewById<RecyclerView>(R.id.recycler_view).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewModelAdapter
        }


        // mengobservasi kesalahan jaringan
        viewModel.eventNetworkError.observe(viewLifecycleOwner, Observer<Boolean> { isNetworkError ->
            if (isNetworkError) onNetworkError()
        })

        return binding.root
    }

   // /**
   //     * Metode untuk menampilkan pesan kesalahan Toast untuk kesalahan jaringan.
   //     */
    private fun onNetworkError() {
        if(!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }

    // untuk mengenerate sumber aplikasi youtube
    private val DevByteVideo.launchUri: Uri
        get() {
            val httpUri = Uri.parse(url)
            return Uri.parse("vnd.youtube:" + httpUri.getQueryParameter("v"))
        }
}

// membuat class VideoClick saat video di klik atau dipilih
class VideoClick(val block: (DevByteVideo) -> Unit) {
    fun onClick(video: DevByteVideo) = block(video)
}

// Adaptor RecyclerView untuk menyiapkan pengikatan data pada item dalam daftar.
class DevByteAdapter(val callback: VideoClick) : RecyclerView.Adapter<DevByteViewHolder>() {

   // Video yang akan ditampilkan menggunakan Adaptor
    var videos: List<DevByteVideo> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    // Dipanggil saat RecyclerView membutuhkan link viewHolder baru dari jenis tertentu untuk
    // direpresentasikan
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DevByteViewHolder {
        val withDataBinding: DevbyteItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                DevByteViewHolder.LAYOUT,
                parent,
                false)
        return DevByteViewHolder(withDataBinding)
    }

    override fun getItemCount() = videos.size

    // Dipanggil oleh RecyclerView untuk menampilkan data pada posisi yang ditentukan.
    override fun onBindViewHolder(holder: DevByteViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.video = videos[position]
            it.videoCallback = callback
        }
    }

}

// ViewHolder untuk item DevByte. Semua pekerjaan dilakukan dengan pengikatan data.
class DevByteViewHolder(val viewDataBinding: DevbyteItemBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.devbyte_item
    }
}