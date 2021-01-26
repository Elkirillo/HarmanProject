package com.example.finalproject.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.finalproject.R
import com.example.finalproject.model.User
import com.example.finalproject.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.fragment_update.*
import kotlinx.android.synthetic.main.fragment_update.view.*

class UpdateFragment : Fragment() {

    private val args by navArgs<UpdateFragmentArgs>()
    private lateinit var mUserViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update, container, false)

        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        view.updateFirstName_et.setText(args.currentUser.place)
        view.updateLastName_et.setText(args.currentUser.myEvent)
        view.updateTextNumber.setText(args.currentUser.myTime)

        view.update_btn.setOnClickListener {
            updateItem()
        }
        //добавляем menu
        setHasOptionsMenu(true)
        return view
    }

    private fun updateItem() {
        val place = updateFirstName_et.text.toString()
        val myEvent = updateLastName_et.text.toString()
        val myTime = updateTextNumber.text.toString()
        if (inputCheck(place, myEvent, myTime)) {
            //Создаем объект пользователя
            val updateUser = User(args.currentUser.id, place, myEvent, myTime)
            //Обновляем текущего пользователя
            mUserViewModel.updateUser(updateUser)
            Toast.makeText(requireContext(), "Успешно обновлены данные!", Toast.LENGTH_LONG).show()
            //Возращаемся обратно
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        } else {
            Toast.makeText(requireContext(), "Не все заполнены поля!", Toast.LENGTH_LONG).show()
        }
    }

    private fun inputCheck(place: String, myEvent: String,  myTime: String): Boolean {
        return !(TextUtils.isEmpty(place) || TextUtils.isEmpty(myEvent) || TextUtils.isEmpty(myTime))

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_delete) {
            deleteUser()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteUser() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Да"){ _, _->
            mUserViewModel.deleteUser(args.currentUser)
            Toast.makeText(requireContext(),
                    "Упоминание ${args.currentUser.place} успешно удалено",
                    Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton("Нет") { _, _ -> }
        builder.setTitle("Удалить напоминание ${args.currentUser.place}?")
        builder.setMessage("Вы уверенны, что хотите удалить напоминание ${args.currentUser.place}?")
        builder.create().show()
    }
}