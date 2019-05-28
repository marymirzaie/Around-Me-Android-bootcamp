package com.workshop.aroundme.app.ui.home.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.workshop.aroundme.R
import com.workshop.aroundme.app.MyApp
import com.workshop.aroundme.app.ui.detail.DetailFragment
import com.workshop.aroundme.app.ui.home.OnHomePlaceItemClickListener
import com.workshop.aroundme.app.ui.home.view.adapters.ModernHomeAdapter
import com.workshop.aroundme.data.model.PlaceEntity
import javax.inject.Inject

class HomeFragment : Fragment(), OnHomePlaceItemClickListener {

    private var adapter: ModernHomeAdapter? = null
    @Inject
    lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyApp.component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        viewModel.apply {
            places.observe(this@HomeFragment, Observer {
                println(it)
                val progressBar = view.findViewById<ProgressBar>(R.id.loadingBar)
                progressBar?.visibility = View.GONE
                adapter = ModernHomeAdapter(it, this@HomeFragment)
                recyclerView?.adapter = adapter
            })
            categories.observe(this@HomeFragment, Observer {
                adapter?.parentCategories = it
            })
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.onActivityCreated()

    }

    override fun onPlaceItemCliced(placeEntity: PlaceEntity) {
        fragmentManager?.beginTransaction()
            ?.replace(R.id.content_frame, DetailFragment.newInstance(placeEntity.slug))
            ?.addToBackStack(null)
            ?.commit()
    }

    override fun onItemStarred(placeEntity: PlaceEntity) {
//        placeRepository.starPlace(placeEntity)
    }
}
