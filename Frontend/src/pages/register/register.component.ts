import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router'; // 引入 Router 服務
import { log } from 'console';
import { AuthService } from '../../services/auth.service';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { AuthResponseDto } from '../../entity/auth-response.dto';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
  imports: [CommonModule, FormsModule, RouterLink],
})
export class RegisterComponent implements OnInit {
  username: string = '';
  password: string = '';
  confirmPassword: string = '';

  isModalVisible: boolean = false;
  modalMessage: string = '';

  constructor(private http: HttpClient, private router: Router) {} // 注入 Router 服務

  ngOnInit(): void {
    console.log('進入註冊頁面');
  }

  /**
   * 處理註冊表單提交
   * 根據安全原則：所有透過此頁面註冊的使用者預設為普通使用者 (user)。
   * 管理員角色將由後台系統的現有管理員手動分配。
   */
  onRegister(): void {
    if (
      this.username.trim() === '' ||
      this.password.trim() === '' ||
      this.confirmPassword.trim() === ''
    ) {
      this.modalMessage = '所有欄位都不能為空！';
      this.isModalVisible = true;
      return;
    }

    if (this.password !== this.confirmPassword) {
      this.modalMessage = '密碼與確認密碼不一致！';
      this.isModalVisible = true;
      return;
    }
    const payload = {
      username: this.username,
      password: this.password,
      roles: ['user'],
    };
    this.http
      .post('http://localhost:8080/auth/register', payload, {
        responseType: 'text',
      })
      .subscribe({
        next: (msg) => {
          this.showModal(msg); // msg 是 "User signed up successfully"
          setTimeout(() => {
            this.closeModal();
            this.router.navigate(['/home']);
          }, 3000);
        },
        error: (err: HttpErrorResponse) => {
          const msg =
            typeof err.error === 'string'
              ? err.error
              : err.error?.message || '註冊失敗';
          this.showModal(msg);
          console.error(err);
        },
      });
  }

  private showModal(msg: string) {
    this.modalMessage = msg;
    this.isModalVisible = true;
  }
  /**
   * 關閉模態彈窗
   */
  closeModal(): void {
    this.isModalVisible = false;
  }
}
