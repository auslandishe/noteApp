import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs'; // 用於管理訂閱
import { StickyNoteService, StickyNote } from '../../services/sticky-note.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
@Component({
  selector: 'app-sticky-note',
  templateUrl: './sticky-note.component.html',
  styleUrls: ['./sticky-note.component.css'],
  imports:[CommonModule, FormsModule]
})
export class StickyNoteComponent implements OnInit, OnDestroy {

  stickyNotes: StickyNote[] = []; // 儲存所有便利貼的陣列
  newNoteContent: string = ''; // 用於新增便利貼的輸入框綁定
  private notesSubscription: Subscription | undefined; // 用於管理對服務的訂閱

  // 模態彈窗相關屬性
  isModalVisible: boolean = false;
  modalMessage: string = '';
  modalType: 'alert' | 'confirm' = 'alert'; // 'alert' for simple messages, 'confirm' for deletion
  noteToDeleteId: string | null = null; // 儲存待刪除的便利貼 ID

  constructor(private stickyNoteService: StickyNoteService) { } // 注入 StickyNoteService

  ngOnInit(): void {
    // 在元件初始化時，訂閱 StickyNoteService 的便利貼數據流
    this.notesSubscription = this.stickyNoteService.getNotes().subscribe(notes => {
      this.stickyNotes = notes; // 更新本地的便利貼陣列
      console.log('便利貼已更新:', this.stickyNotes);
    });
  }

  ngOnDestroy(): void {
    // 在元件銷毀時，取消訂閱以防止內存洩漏
    if (this.notesSubscription) {
      this.notesSubscription.unsubscribe();
    }
  }

  /**
   * 新增一個便利貼
   */
  addNote(): void {
    const content = this.newNoteContent.trim();
    if (content) {
      this.stickyNoteService.addNote(content); // 通過服務新增便利貼
      this.newNoteContent = ''; // 清空輸入框
      this.modalMessage = '便利貼已成功新增！';
      this.modalType = 'alert';
      this.isModalVisible = true;
    } else {
      this.modalMessage = '請輸入便利貼內容！';
      this.modalType = 'alert';
      this.isModalVisible = true;
    }
  }

  /**
   * 彈出確認刪除便利貼的模態框
   * @param id 要刪除的便利貼 ID
   */
  confirmDelete(id: string): void {
    this.noteToDeleteId = id;
    this.modalMessage = '您確定要刪除這張便利貼嗎？';
    this.modalType = 'confirm';
    this.isModalVisible = true;
  }

  /**
   * 執行便利貼刪除操作
   */
  performDelete(): void {
    if (this.noteToDeleteId) {
      this.stickyNoteService.deleteNote(this.noteToDeleteId); // 通過服務刪除便利貼
      this.modalMessage = '便利貼已成功刪除！';
      this.modalType = 'alert';
      this.noteToDeleteId = null; // 清空待刪除 ID
    }
    // 保持模態框開啟以顯示成功訊息，直到使用者點擊「確定」
  }

  /**
   * 關閉模態彈窗
   */
  closeModal(): void {
    this.isModalVisible = false;
    this.noteToDeleteId = null; // 確保清空待刪除 ID
  }
}
