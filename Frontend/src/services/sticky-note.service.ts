import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

// 定義便利貼的數據結構
export interface StickyNote {
  id: string;       // 唯一識別符
  content: string;  // 便利貼內容
  date: string;     // 建立日期字串
  color: string;    // 便利貼的背景顏色類別 (例如 'bg-yellow-200')
}

@Injectable({
  providedIn: 'root' // 服務在整個應用程式中可用
})
export class StickyNoteService {
  // 使用 BehaviorSubject 來儲存和發送便利貼列表
  private stickyNotes: StickyNote[] = [];
  private notesSubject: BehaviorSubject<StickyNote[]> = new BehaviorSubject(this.stickyNotes);

  // 定義一組柔和的背景顏色供便利貼使用
  private availableColors: string[] = [
    'bg-yellow-200', 'bg-blue-200', 'bg-green-200', 'bg-pink-200',
    'bg-purple-200', 'bg-red-200', 'bg-indigo-200', 'bg-teal-200'
  ];

  constructor() {
    // 可以在這裡初始化數據，例如從 localStorage 載入
    // 目前為了演示，它會從空列表開始
  }

  /**
   * 新增一個便利貼到列表中
   * @param content 便利貼的內容
   */
  addNote(content: string): void {
    const newNote: StickyNote = {
      id: Date.now().toString(), // 使用時間戳作為簡單的唯一 ID
      content: content,
      date: new Date().toLocaleDateString('zh-TW'), // 簡潔的日期格式
      color: this.getRandomColor() // 隨機分配一個顏色
    };
    this.stickyNotes.push(newNote);
    this.notesSubject.next(this.stickyNotes); // 發送更新後的列表給所有訂閱者
  }

  /**
   * 刪除指定 ID 的便利貼
   * @param id 要刪除的便利貼 ID
   */
  deleteNote(id: string): void {
    this.stickyNotes = this.stickyNotes.filter(note => note.id !== id);
    this.notesSubject.next(this.stickyNotes); // 發送更新後的列表
  }

  /**
   * 獲取便利貼的可觀察對象
   * 訂閱此對象以接收便利貼列表的實時更新
   * @returns 包含便利貼陣列的 Observable
   */
  getNotes(): Observable<StickyNote[]> {
    return this.notesSubject.asObservable();
  }

  /**
   * 隨機獲取一個顏色類別
   * @returns Tailwind CSS 顏色類別字串
   */
  private getRandomColor(): string {
    const randomIndex = Math.floor(Math.random() * this.availableColors.length);
    return this.availableColors[randomIndex];
  }

  /**
   * 清空所有便利貼 (僅用於測試或特定功能)
   */
  clearNotes(): void {
    this.stickyNotes = [];
    this.notesSubject.next(this.stickyNotes);
  }
}
