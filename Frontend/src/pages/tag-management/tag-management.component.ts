import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-tag-management',
  templateUrl: './tag-management.component.html',
  styleUrls: ['./tag-management.component.css'],
  imports:[CommonModule, FormsModule]
})
export class TagManagementComponent implements OnInit {

  // 儲存所有標籤的陣列
  tags: string[] = ['科技', '生活', '程式設計', '學習', '健康', '美食']; // 初始範例標籤
  newTagInput: string = ''; // 用於新增標籤的輸入框綁定

  // 模態彈窗相關屬性
  isModalVisible: boolean = false;
  modalMessage: string = '';
  modalType: 'alert' | 'confirm' = 'alert'; // 'alert' for simple messages, 'confirm' for deletion
  tagToDelete: string | null = null; // 儲存待刪除的標籤名稱

  // 標籤顏色列表 (與其他元件保持一致)
  tagColors: string[] = [
    'bg-blue-500', 'bg-green-500', 'bg-red-500', 'bg-yellow-500',
    'bg-indigo-500', 'bg-purple-500', 'bg-pink-500', 'bg-teal-500'
  ];

  constructor() { }

  ngOnInit(): void {
    // 可以在這裡從服務或本地儲存載入現有標籤
  }

  /**
   * 根據標籤內容獲取 Tailwind CSS 顏色類別
   * @param tag 標籤字串
   * @returns 對應的 Tailwind CSS 顏色類別
   */
  getTagColor(tag: string): string {
    let hash = 0;
    for (let i = 0; i < tag.length; i++) {
      hash = tag.charCodeAt(i) + ((hash << 5) - hash);
    }
    const index = Math.abs(hash % this.tagColors.length);
    return this.tagColors[index];
  }

  /**
   * 新增一個新標籤
   */
  addTag(): void {
    const tag = this.newTagInput.trim();
    if (tag) {
      if (this.tags.includes(tag)) {
        this.modalMessage = `標籤 "${tag}" 已存在！`;
        this.modalType = 'alert';
        this.isModalVisible = true;
      } else {
        this.tags.push(tag);
        this.newTagInput = ''; // 清空輸入框
        // 可以在這裡呼叫服務來儲存標籤到後端
        this.modalMessage = `標籤 "${tag}" 已成功新增！`;
        this.modalType = 'alert';
        this.isModalVisible = true;
      }
    } else {
      this.modalMessage = '請輸入標籤名稱！';
      this.modalType = 'alert';
      this.isModalVisible = true;
    }
  }

  /**
   * 彈出確認刪除標籤的模態框
   * @param tag 要刪除的標籤名稱
   */
  confirmDelete(tag: string): void {
    this.tagToDelete = tag;
    this.modalMessage = `您確定要刪除標籤 "${tag}" 嗎？`;
    this.modalType = 'confirm';
    this.isModalVisible = true;
  }

  /**
   * 執行標籤刪除操作
   */
  performDelete(): void {
    if (this.tagToDelete) {
      this.tags = this.tags.filter(t => t !== this.tagToDelete);
      this.modalMessage = `標籤 "${this.tagToDelete}" 已成功刪除！`;
      this.modalType = 'alert';
      // 可以在這裡呼叫服務來從後端刪除標籤
      this.tagToDelete = null; // 清空待刪除標籤
    }
    // 保持模態框開啟以顯示成功訊息，直到使用者點擊「確定」
  }

  /**
   * 關閉模態彈窗
   */
  closeModal(): void {
    this.isModalVisible = false;
    this.tagToDelete = null; // 確保清空待刪除標籤
  }
}
