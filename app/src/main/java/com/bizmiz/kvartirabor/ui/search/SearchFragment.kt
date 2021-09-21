package com.bizmiz.kvartirabor.ui.search

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bizmiz.kvartirabor.R
import com.bizmiz.kvartirabor.data.*
import com.bizmiz.kvartirabor.data.Adapters.ElonlarAdapter
import com.bizmiz.kvartirabor.databinding.FragmentSearchBinding
import com.bizmiz.kvartirabor.ui.MainActivity
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.app.Activity
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat

import androidx.core.content.ContextCompat.getSystemService
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation


class SearchFragment : Fragment(R.layout.fragment_search) {
    private val searchViewModel: SearchViewModel by viewModel()
    private lateinit var elonlarAdapter: ElonlarAdapter
    var key = true
    var check = true
    var minSumm = 0
    var maxSumm = 0
    var viloyatNomi = Constant.DEFAULT_ITEM
    var bolimNomi = Constant.DEFAULT_BOLIM
    var valyuta = Constant.DEFAULT_VALYUTA
    var viloyatId = 0
    var bolimID = 0
    var valyutaId = 0
    var searchVilovatlar: ArrayList<String> = arrayListOf(
        "O'zbekiston",
        "Andijon viloyati",
        "Buxoro viloyati",
        "Jizzax viloyati",
        "Qoraqalpog'iston",
        "Qashqadaryo viloyati",
        "Navoiy viloyati",
        "Namangan viloyati",
        "Samarqand viloyati",
        "Surxondaryo viloyati",
        "Sirdaryo viloyati",
        "Toshkent viloyati",
        "Farg'ona viloyati",
        "Xorazm viloyati"
    )
    private var spinnerViloyatlarItem = Constant.DEFAULT_ITEM
    private lateinit var binding: FragmentSearchBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)
        (activity as MainActivity).visibility(true)
        binding.apply {
            if (check){
                etSearch.showSoftKeyboard()
            }
            binding.imgBack.setOnClickListener {
                val navController: NavController =
                    Navigation.findNavController(
                        requireActivity(),
                        R.id.mainFragmentContener
                    )
                navController.popBackStack()
                (activity as MainActivity).binding.bottomNavView.selectedItemId = R.id.elonlarFragment
            }
            etSearch.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideKeyboard(v)
                    if (etSearch.text.isNotEmpty()) {
                        val query = etSearch.text.toString()
                        searchViewModel.getSearchData(
                            key,
                            query,
                            bolimNomi,
                            viloyatNomi,
                            valyuta,
                            minSumm,
                            maxSumm
                        )
                    } else {
                        binding.recView.visibility = View.GONE
                        binding.txtEslatma.text = "Qidiruv natijalari shu yerda ko'rinadi"
                    }

                    return@OnEditorActionListener true
                }
                false
            })
            elonlarAdapter = ElonlarAdapter()
            observe()
            imgFilter.setOnClickListener {
                val spinnerAdapter =
                    ArrayAdapter(requireActivity(), R.layout.spinner_item, searchVilovatlar)
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                val bolimAdapter = ArrayAdapter(requireActivity(), R.layout.spinner_item, listBolim)
                bolimAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                val valyutaAdapter =
                    ArrayAdapter(requireActivity(), R.layout.spinner_item, pulBirlikSearch)
                valyutaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                val builder = AlertDialog.Builder(
                    requireActivity()
                )
                val customLayout = layoutInflater.inflate(R.layout.filter_view, null)
                val spinnerVil = customLayout.findViewById<Spinner>(R.id.spViloyatlar)
                val spinnerBolim = customLayout.findViewById<Spinner>(R.id.spBolimlar)
                val spinnnerValyuta = customLayout.findViewById<Spinner>(R.id.spValyuta)
                val startNarx = customLayout.findViewById<EditText>(R.id.etStartNarx)
                val endNarx = customLayout.findViewById<EditText>(R.id.etEndNarx)
                val switchView = customLayout.findViewById<SwitchCompat>(R.id.switchView)
                if (minSumm != 0 || maxSumm != 0) {
                    switchView.isChecked = true
                    startNarx.setText(minSumm.toString())
                    endNarx.setText(maxSumm.toString())
                    startNarx.isEnabled = true
                    endNarx.isEnabled = true
                }
                switchView.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (isChecked) {
                        if (minSumm != 0 || maxSumm != 0) {
                            startNarx.setText(minSumm.toString())
                            endNarx.setText(maxSumm.toString())
                        }
                        startNarx.isEnabled = true
                        endNarx.isEnabled = true
                    } else {
                        startNarx.text.clear()
                        minSumm = 0
                        maxSumm = 0
                        endNarx.text.clear()
                        startNarx.isEnabled = false
                        endNarx.isEnabled = false
                    }
                }
                spinnerVil.adapter = spinnerAdapter
                spinnerBolim.adapter = bolimAdapter
                spinnnerValyuta.adapter = valyutaAdapter
                spinnerVil.setSelection(viloyatId)
                spinnerBolim.setSelection(bolimID)
                spinnnerValyuta.setSelection(valyutaId)
                val sarlavha = customLayout.findViewById<RadioButton>(R.id.rdbSarlavhaOrqali)
                val tavsif = customLayout.findViewById<RadioButton>(R.id.rdbTavsifOrqali)
                sarlavha.isChecked = key
                tavsif.isChecked = !key
                sarlavha.setOnClickListener {
                    twoCheckRadioButton(sarlavha, tavsif)
                }
                tavsif.setOnClickListener {
                    twoCheckRadioButton(tavsif, sarlavha)
                }
                builder.setView(customLayout)
                val message = builder.create()
                message.show()
                customLayout.findViewById<TextView>(R.id.txtSaqlash).setOnClickListener { view ->
                    when {
                        sarlavha.isChecked -> {
                            key = true
                        }
                        tavsif.isChecked -> {
                            key = false
                        }
                    }
                    if (switchView.isChecked) {
                        if (startNarx.text.isNotEmpty() && endNarx.text.isNotEmpty()) {
                            if (startNarx.text.toString().toInt() < endNarx.text.toString()
                                    .toInt()
                            ) {
                                minSumm = startNarx.text.toString().toInt()
                                maxSumm = endNarx.text.toString().toInt()
                                valyuta = spinnnerValyuta.selectedItem.toString()
                                viloyatNomi = spinnerVil.selectedItem.toString()
                                bolimNomi = spinnerBolim.selectedItem.toString()
                                viloyatId = spinnerVil.selectedItemPosition
                                bolimID = spinnerBolim.selectedItemPosition
                                valyutaId = spinnnerValyuta.selectedItemPosition
                                spinnerViloyatlarItem = spinnerVil.selectedItem.toString()
                                if (etSearch.text.isNotEmpty()) {
                                    val query = etSearch.text.toString()
                                    searchViewModel.getSearchData(
                                        key,
                                        query,
                                        bolimNomi,
                                        viloyatNomi,
                                        valyuta,
                                        minSumm,
                                        maxSumm
                                    )
                                }
                                message.dismiss()
                            } else {
                                Snackbar.make(
                                    view,
                                    "Xato! Boshlang'ich narx oxirgi narxdan kichik bolishi kerak",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Snackbar.make(
                                view,
                                "Xato! Boshlang'ich narx va oxirgi narxni kiriting",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }

                    } else {
                        viloyatNomi = spinnerVil.selectedItem.toString()
                        bolimNomi = spinnerBolim.selectedItem.toString()
                        viloyatId = spinnerVil.selectedItemPosition
                        bolimID = spinnerBolim.selectedItemPosition
                        valyutaId = spinnnerValyuta.selectedItemPosition
                        spinnerViloyatlarItem = spinnerVil.selectedItem.toString()
                        if (etSearch.text.isNotEmpty()) {
                            val query = etSearch.text.toString()
                            searchViewModel.getSearchData(
                                key,
                                query,
                                bolimNomi,
                                viloyatNomi,
                                valyuta,
                                minSumm,
                                maxSumm
                            )
                        }
                        message.dismiss()
                        binding.recView.visibility = View.GONE
                        binding.txtEslatma.text = "Qidiruv natijalari shu yerda ko'rinadi"
                    }


                }
            }
        }
    }
    fun hideKeyboard(view: View) {
        val inputMethodManager: InputMethodManager? =
            requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
    }
    private fun observe() {
        searchViewModel.search.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                ResourceState.SUCCESS -> {
                    elonlarAdapter.models = it.data!!
                    if (it.data.isNullOrEmpty()) {
                        binding.recView.visibility = View.GONE
                        binding.txtEslatma.text = "Natija topilmadi"
                    } else {
                        binding.recView.visibility = View.VISIBLE
                        binding.recView.adapter = elonlarAdapter
                        elonlarAdapter.setOnClickListener { data ->
                            check = false
                            val bundle = bundleOf(
                                "id" to data.id,
                                "sarlavha" to data.sarlavha,
                                "bolim" to data.bolim,
                                "uyQavatliligi" to data.uyQavatliligi,
                                "umumiyMaydon" to data.umumiyMaydon,
                                "oshxonaMaydoni" to data.oshxonaMaydoni,
                                "uyTamiri" to data.uyTamiri,
                                "yashashMaydoni" to data.yashashMaydoni,
                                "narxi" to data.narxi,
                                "yashashQavati" to data.yashashQavati,
                                "xonaSoni" to data.xonaSoni,
                                "tavsif" to data.tavsif,
                                "telRaqam" to data.telRaqam,
                                "type" to data.type,
                                "mebel" to data.mebel,
                                "kelishuv" to data.kelishuv,
                                "sharoitlari" to data.sharoitlari,
                                "qurilishTuri" to data.qurilishTuri,
                                "imageUrlList" to data.imageUrlList,
                                "createdDate" to data.createdDate,
                                "latitude" to data.latitude,
                                "longitude" to data.longitude,
                                "viloyat" to data.viloyat
                            )
                            val navController: NavController =
                                Navigation.findNavController(
                                    requireActivity(),
                                    R.id.mainFragmentContener
                                )
                            navController.navigate(
                                R.id.action_searchFragment_to_elonFullFragment,
                                bundle
                            )
                        }
                    }

                }
                ResourceState.ERROR -> {
                    binding.recView.visibility = View.GONE
                    Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
