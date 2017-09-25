import { Component, OnInit } from '@angular/core';
import {AccountService} from '../shared/auth/account.service';
import {Account} from '../shared/user/account.model';
import {TodoService} from '../entities/todo/todo.service';
import {Todo} from '../entities/todo/todo.model';
import {TodoListService} from '../entities/todo-list/todo-list.service';
import {TodoList} from '../entities/todo-list/todo-list.model';

@Component({
  selector: 'jhi-todohome',
  templateUrl: './todohome.component.html',
  styles: []
})
export class TodohomeComponent implements OnInit {

  constructor(private accountService: AccountService,
              private todoService: TodoService,
              private todoListService: TodoListService) { }

  accountData :Account;
  lists: TodoList[];
  todos: Todo[];

  currentList: TodoList;


  newItem: string;
  newList: string;

  ngOnInit() {
      this.accountService.get()
          .subscribe((acc) => {
            this.accountData = acc;
          });

      this.todoService.query()
          .subscribe((todoList) => {
            this.todos = todoList.json;
          })
  }


  addList() {
      this.todoListService.create({name: this.newList})
          .subscribe((createdList)=> {
            this.newList = null;
            this.lists.push(createdList);
            this.changeList(createdList.id);
          })
  }

  addItem() {
      this.todoService.create({text: this.newItem})
          .subscribe((createdItem)=> {
            this.newItem = null;
            this.todos.push(createdItem);
          })
  }

  changeList(listId){
      const chosenList = this.lists.filter((todoList)=> todoList.id == listId)[0];
      this.currentList = chosenList;
      this.loadElements(this.currentList.id);
  }

  private loadElements(listId){
    return [];
  }

}
