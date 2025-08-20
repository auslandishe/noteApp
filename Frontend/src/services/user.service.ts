import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

// 定義使用者數據結構
export interface User {
  id: string;
  username: string;
  role: 'user' | 'admin'; // 角色類型限於 'user' 或 'admin'
}

@Injectable({
  providedIn: 'root' // 服務在整個應用程式中可用
})
export class UserService {
  // 模擬的初始使用者數據
  // 為了演示，我們預設有一個 admin 和幾個 user
  private initialUsers: User[] = [
    { id: 'user-1', username: 'admin@example.com', role: 'admin' },
    { id: 'user-2', username: 'user1@example.com', role: 'user' },
    { id: 'user-3', username: 'user2@example.com', role: 'user' },
    { id: 'user-4', username: 'test@example.com', role: 'user' } // 這是我們註冊頁面模擬的預設用戶
  ];

  // 使用 BehaviorSubject 來儲存和發送使用者列表
  private users: BehaviorSubject<User[]> = new BehaviorSubject(this.initialUsers);

  constructor() { }

  /**
   * 獲取使用者列表的可觀察對象
   * 訂閱此對象以接收使用者列表的實時更新
   * @returns 包含使用者陣列的 Observable
   */
  getUsers(): Observable<User[]> {
    return this.users.asObservable();
  }

  /**
   * 更新指定使用者的角色
   * @param userId 要更新的使用者 ID
   * @param newRole 新的角色
   */
  updateUserRole(userId: string, newRole: 'user' | 'admin'): void {
    const currentUsers = this.users.getValue();
    const updatedUsers = currentUsers.map(user =>
      user.id === userId ? { ...user, role: newRole } : user
    );
    this.users.next(updatedUsers); // 發送更新後的列表
    console.log(`使用者 ${userId} 的角色已更新為 ${newRole}`);
  }

  /**
   * 刪除指定使用者
   * @param userId 要刪除的使用者 ID
   */
  deleteUser(userId: string): void {
    const currentUsers = this.users.getValue();
    const filteredUsers = currentUsers.filter(user => user.id !== userId);
    this.users.next(filteredUsers); // 發送更新後的列表
    console.log(`使用者 ${userId} 已被刪除`);
  }

  /**
   * 模擬新增使用者 (例如從註冊頁面過來)
   * 在真實後端，這會是註冊 API 的一部分
   * @param newUser 要新增的使用者對象
   */
  addUser(newUser: User): void {
    const currentUsers = this.users.getValue();
    // 檢查是否已存在同名用戶（簡化，真實情況會更嚴格）
    if (!currentUsers.some(user => user.username === newUser.username)) {
      this.users.next([...currentUsers, newUser]);
      console.log(`新使用者 ${newUser.username} 已新增`);
    } else {
      console.warn(`使用者 ${newUser.username} 已存在，無法新增。`);
    }
  }
}
