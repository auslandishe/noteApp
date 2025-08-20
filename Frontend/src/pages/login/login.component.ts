// src/app/login/login.component.ts
import { HttpClient, HttpClientModule, HttpErrorResponse } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthResponseDto } from '../../entity/auth-response.dto'; // 假設路徑正確
import { AuthService } from '../../services/auth.service'; // 假設路徑正確

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  standalone: true,
  imports: [FormsModule, CommonModule, HttpClientModule, RouterLink] // 確保 RouterLink 也在 imports 中
})
export class LoginComponent implements OnInit {

  userName: string = ''; // 這個屬性名稱保持不變，因為它與 HTML 的 ngModel 綁定
  password: string = '';

  isModalVisible: boolean = false;
  modalMessage: string = '';

  constructor(
    private http: HttpClient,
    private router: Router,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
  }

  /**
   * 處理登入表單提交
   */
  onLogin(): void {
    if (this.userName.trim() === '' || this.password.trim() === '') {
      this.showModal('帳號和密碼都不能為空！');
      return;
    }

    // ⚠️ 關鍵修改：將 payload 中的 "userName" 改為 "username" (小寫 u)
    const payload = {
      "username": this.userName, // <--- 這裡改為 "username"
      "password": this.password,
    };

    this.http.post<AuthResponseDto>('http://localhost:8080/auth/login', payload)
      .subscribe({
        next: (res) => {
          console.log('LoginComponent: 從後端收到登入成功回應。');
          this.authService.login(res.token, this.userName);

          this.showModal('登入成功！');
          this.closeModal();
        },
        error: (err: HttpErrorResponse) => {
          const msg = err.error?.message || '登入失敗，請稍後再試';
          this.showModal(msg);
          console.error('LoginComponent: 登入失敗:', err);
          this.authService.setLoginStatus(false, null, []);
        }
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
