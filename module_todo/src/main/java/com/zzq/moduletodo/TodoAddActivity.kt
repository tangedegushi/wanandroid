package com.zzq.moduletodo

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Menu
import android.view.MenuItem
import com.zzq.commonlib.base.BaseActivity
import com.zzq.moduletodo.model.TodoAddModel
import kotlinx.android.synthetic.main.activity_todo_add.*
import android.app.DatePickerDialog
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import com.zzq.moduletodo.bean.TodoData
import com.zzq.netlib.utils.Logger
import com.zzq.netlib.utils.UtilApp
import java.lang.StringBuilder
import java.util.*


class TodoAddActivity : BaseActivity() {
    override val useBar: Boolean = true
    override val homeUp: Boolean = true
    private lateinit var todoTitle: String
    private var todoContent: String? = null
    private var todoTime: String? = null
    private val todoAddModel by lazy { TodoAddModel(this) }
    private var todoItemData: TodoData.DatasBean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_add)
        val itemData = intent.getSerializableExtra(TODOITEMDATA)
        itemData?.apply {
            todoItemData = itemData as TodoData.DatasBean
        }

        todoAddModel.liveTodoAddData.observe(this, Observer {
            it?.apply {
                setResult(ADD_SUCCESS)
                finish()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        todoItemData?.apply {
            toolbar?.title = getString(R.string.todo_detail)
        }
    }

    override fun onContentChanged() {
        super.onContentChanged()
        tv_todo_date.setOnClickListener { _ ->
            val calendar = Calendar.getInstance()

            val datePickerDialog = DatePickerDialog(this,
                    DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                        tv_todo_date.text = StringBuilder().append(year)
                                .append("-").append(if (month < 9) "0${month+1}" else "${month+1}")
                                .append("-").append(dayOfMonth).toString()
                        todoItemData?.apply {
                            if (dateStr != tv_todo_date.text.toString() || isContentChange) {
                                menu?.apply {
                                    if (size() != 0 && getItem(size() - 1).title == getString(R.string.todo_update)) return@OnDateSetListener
                                }
                                menu?.clear()
                                menuInflater.inflate(R.menu.menu_todo_update, menu)
                            } else {
                                menu?.apply {
                                    if (size() != 0 && getItem(size() - 1).title == getString(R.string.todo_complete)) return@OnDateSetListener
                                }
                                menu?.clear()
                                menuInflater.inflate(R.menu.menu_todo_complete, menu)
                            }
                        }
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH))
            datePickerDialog.datePicker.minDate = Date().time
            datePickerDialog.show()
        }

        ll_todo_add.setOnClickListener { _ ->
            UtilApp.hideSoftKeyboard(this, ll_todo_add)
        }

        tv_todo_name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (todoItemData == null) {
                    if (s.isEmpty()) {
                        menu?.clear()
                        isMenuCreate = false
                    } else {
                        if (isMenuCreate) return
                        isMenuCreate = true
                        menuInflater.inflate(R.menu.menu_todo_add, menu)
                    }
                } else {
                    dealContentChanger(true, s)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Logger.zzqLog().i("beforeTextChanged ${s.toString()}  $s")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Logger.zzqLog().i("onTextChanged ${s.toString()}  $s")
            }
        })
        tv_todo_content.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (todoItemData != null) {
                    dealContentChanger(false, s)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })
    }

    private fun dealContentChanger(isTitle: Boolean, s: Editable) {
        if (todoItemData!!.status == 1) return
        if (s.isEmpty()) {
            menu?.clear()
            isMenuCreate = false
        } else {
            val isChange = if (isTitle) {
                !(todoItemData!!.title == s.toString() && todoItemData!!.content == tv_todo_content.text.toString())
            } else {
                !(todoItemData!!.title == tv_todo_name.text.toString() && todoItemData!!.content == s.toString())
            }
            if (isContentChange == isChange) return
            isContentChange = isChange

            if (tv_todo_date.text.toString() != todoItemData!!.dateStr) {
                menu?.apply {
                    if (size() != 0 && getItem(size() - 1).title == getString(R.string.todo_update)) return
                    clear()
                    menuInflater.inflate(R.menu.menu_todo_update, menu)
                }
                return
            }

            menu?.clear()
            if (isContentChange) {
                menuInflater.inflate(R.menu.menu_todo_update, menu)
            } else {
                menuInflater.inflate(R.menu.menu_todo_complete, menu)
            }
        }
    }

    private var menu: Menu? = null
    private var isMenuCreate = false
    private var isContentChange = false
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menu = menu
        if (todoItemData != null) {
            if (todoItemData!!.status == 1) {
                menuInflater.inflate(R.menu.menu_todo_update, menu)
            } else {
                menuInflater.inflate(R.menu.menu_todo_complete, menu)
            }
            todoItemData?.apply {
                tv_todo_date.text = dateStr
                tv_todo_name.setText(title)
                tv_todo_name.setSelection(title.length)
                tv_todo_content.setText(content)
                if (status == 0) {
                    rg_todo_type.visibility = View.GONE
                    last_line.visibility = View.GONE
                } else {
                    when (type) {
                        1 -> rb_todo_type_work.isChecked = true
                        2 -> rb_todo_type_study.isChecked = true
                        3 -> rb_todo_type_life.isChecked = true
                    }
                }
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        todoTitle = tv_todo_name.text.toString()
        todoContent = tv_todo_content.text.toString()
        todoTime = tv_todo_date.text.toString()
        when (item.itemId) {
            R.id.todo_add -> {
                if (TextUtils.isEmpty(todoTitle)) {
                    UtilApp.showToast("请输入便签名称")
                    return false
                }
                val mType = when (rg_todo_type.checkedRadioButtonId) {
                    R.id.rb_todo_type_all -> 0
                    R.id.rb_todo_type_work -> 1
                    R.id.rb_todo_type_study -> 2
                    R.id.rb_todo_type_life -> 3
                    else -> 0
                }
                todoAddModel.addTodo(todoTitle, todoContent, todoTime, type = mType)
                return true
            }
            R.id.todo_complete -> {
                todoItemData?.apply {
                    todoAddModel.updateTodoDone(id, 1)
                }
                return true
            }
            R.id.todo_update -> {
                todoItemData?.apply {
                    todoAddModel.updateTodo(id, todoTitle, todoContent, todoTime, type = type)
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val BACK_ADD = 1
        const val BACK_UPDATE = 2
        const val BACK_UPDATE_DONE = 3
        const val ADD_SUCCESS = 101
        private val TODOITEMDATA = "todoItemData"
        fun open(fragment: Fragment, requestCode: Int, todoItemData: TodoData.DatasBean? = null) {
            val intent = Intent(fragment.context, TodoAddActivity::class.java)
            intent.putExtra(TODOITEMDATA, todoItemData)
            fragment.startActivityForResult(intent, requestCode)
            fragment.activity?.overridePendingTransition(R.anim.activity_enter_transition_r, R.anim.activity_exit_transition_l)
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.activity_enter_transition_l, R.anim.activity_exit_transition_r)
    }
}
