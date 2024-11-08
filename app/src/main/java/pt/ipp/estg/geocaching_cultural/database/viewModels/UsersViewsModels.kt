package pt.ipp.estg.geocaching_cultural.database.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ipp.estg.geocaching_cultural.database.AppDatabase
import pt.ipp.estg.geocaching_cultural.database.classes.User
import pt.ipp.estg.geocaching_cultural.database.repositories.UserRepository

class UsersViewsModels (application: Application) : AndroidViewModel(application) {

    val repository : UserRepository
    //val allBooks : LiveData<List<Book>>

    init {
        val db = AppDatabase.getDatabase(application)
        //val restAPI = RetrofitHelper.getInstance().create(BookAPI::class.java)
        repository = UserRepository(db.UserDao(),/*restAPI*/)
        //allBooks = repository.getBooks()
        //this.updateBooksOnline()
    }

    fun getTop10Users(): LiveData<List<User>> {
        viewModelScope.launch(Dispatchers.IO){
           // updateBooksOnline();
        }
        return repository.getTop10Users()
    }

    fun insertUser(user:User){
        viewModelScope.launch(Dispatchers.IO){
            repository.insert(user)
        }
    }

    fun updateUser(user:User){
        viewModelScope.launch(Dispatchers.IO){
            repository.update(user)
        }
    }

    fun deleteUser(user:User){
        viewModelScope.launch(Dispatchers.IO){
            repository.delete(user)
        }
    }

    fun getUser(id:Int): LiveData<User> {
        return repository.getUser(id)
    }

    /* TODO: adicionar rest api*/
    /*fun updateBooksOnline(){
        viewModelScope.launch(Dispatchers.IO){
            val response = repository.updateBooksOnline()
            if (response.isSuccessful){
                val content = response.body()
                content?.docs?.forEach {
                    insertBook(User(it.key,it.title,it.author_name.toString(),it.year))
                }
            }
        }
    }*/

}
