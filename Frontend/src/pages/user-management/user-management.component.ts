import { Component, OnInit, OnDestroy } from '@angular/core';
import { UserService, User } from '../../services/user.service'; // 引入 UserService 和 User 接口
import { Subscription } from 'rxjs';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-user-management',
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.css'] ,
  imports:[CommonModule, FormsModule]
})
export class UserManagementComponent implements OnInit, OnDestroy {

  users: User[] = []; // 儲存使用者列表
  roles: ('user' | 'admin')[] = ['user', 'admin']; // 可用的角色

  private usersSubscription: Subscription | undefined; // 用於管理對服務的訂閱

  // 模態彈窗相關屬性
  isModalVisible: boolean = false;
  modalMessage: string = '';
  modalType: 'alert' | 'confirm-role-change' | 'confirm-delete' = 'alert';

  // 儲存待處理的使用者資訊，以便在確認後執行操作
  userToActOn: User | null = null;
  newRoleToApply: 'user' | 'admin' | null = null;
  userIdToDelete: string | null = null;
  usernameToDelete: string | null = null;

  constructor(private userService: UserService) { } // 注入 UserService

  ngOnInit(): void {
    // 訂閱 UserService 的使用者數據流
    this.usersSubscription = this.userService.getUsers().subscribe(users => {
      this.users = users; // 更新本地的使用者陣列
      console.log('使用者列表已更新:', this.users);
    });
  }

  ngOnDestroy(): void {
    // 在元件銷毀時，取消訂閱以防止內存洩漏
    if (this.usersSubscription) {
      this.usersSubscription.unsubscribe();
    }
  }

  /**
   * 彈出確認修改角色模態框
   * @param user 被修改角色的使用者
   * @param event 觸發事件 (用於獲取新選中的角色)
   */
  confirmRoleChange(user: User, event: Event): void {
    const selectElement = event.target as HTMLSelectElement;
    const newRole = selectElement.value as 'user' | 'admin';

    // 如果角色沒有實際改變，則不彈出確認
    if (user.role === newRole) {
      return;
    }

    this.userToActOn = user;
    this.newRoleToApply = newRole;
    this.modalMessage = `您確定要將使用者 "${user.username}" 的角色從 "${user.role}" 更改為 "${newRole}" 嗎？`;
    this.modalType = 'confirm-role-change';
    this.isModalVisible = true;
  }

  /**
   * 彈出確認刪除使用者模態框
   * @param userId 要刪除的使用者 ID
   * @param username 要刪除的使用者名稱
   */
  confirmDelete(userId: string, username: string): void {
    this.userIdToDelete = userId;
    this.usernameToDelete = username;
    this.modalMessage = `您確定要刪除使用者 "${username}" 嗎？此操作不可恢復。`;
    this.modalType = 'confirm-delete';
    this.isModalVisible = true;
  }

  /**
   * 執行模態框確認後的操作 (修改角色或刪除)
   */
  performAction(): void {
    if (this.modalType === 'confirm-role-change' && this.userToActOn && this.newRoleToApply) {
      this.userService.updateUserRole(this.userToActOn.id, this.newRoleToApply);
      this.modalMessage = `使用者 "${this.userToActOn.username}" 的角色已成功更新為 "${this.newRoleToApply}"。`;
      this.modalType = 'alert'; // 轉換為提示模式
    } else if (this.modalType === 'confirm-delete' && this.userIdToDelete) {
      this.userService.deleteUser(this.userIdToDelete);
      this.modalMessage = `使用者 "${this.usernameToDelete}" 已成功刪除。`;
      this.modalType = 'alert'; // 轉換為提示模式
    } else {
      this.modalMessage = '操作失敗，請重試。';
      this.modalType = 'alert';
    }
    // 清空待處理的數據
    this.userToActOn = null;
    this.newRoleToApply = null;
    this.userIdToDelete = null;
    this.usernameToDelete = null;
    // 模態框保持開啟以顯示操作結果，直到使用者點擊「確定」
  }

  /**
   * 取消模態框操作，並將角色選擇恢復到原值 (如果適用)
   */
  cancelAction(): void {
    // 如果是角色修改的取消，需要將 select 元素的值恢復
    if (this.modalType === 'confirm-role-change' && this.userToActOn) {
      // 由於 ngModel 已經綁定，這裡不需要手動恢復
      // 只要不執行 performAction，數據就不會被更新
    }
    this.closeModal();
  }

  /**
   * 關閉模態彈窗
   */
  closeModal(): void {
    this.isModalVisible = false;
    // 確保所有待處理的數據都被清除
    this.userToActOn = null;
    this.newRoleToApply = null;
    this.userIdToDelete = null;
    this.usernameToDelete = null;
  }
}
