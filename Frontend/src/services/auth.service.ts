// src/app/core/auth/auth.service.ts
import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { BehaviorSubject, Observable } from 'rxjs';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private _isLoggedIn = new BehaviorSubject<boolean>(false);
  isLoggedIn$: Observable<boolean> = this._isLoggedIn.asObservable();

  private _username = new BehaviorSubject<string | null>(null);
  username$: Observable<string | null> = this._username.asObservable();

  private _userRoles = new BehaviorSubject<string[]>([]);
  userRoles$: Observable<string[]> = this._userRoles.asObservable();

  private readonly TOKEN_KEY = 'token';

  constructor(
    private router: Router,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {
    console.log('AuthService: Constructor called.');
    this.checkInitialLoginStatus();
  }

  private checkInitialLoginStatus(): void {
    if (isPlatformBrowser(this.platformId)) {
      const token = localStorage.getItem(this.TOKEN_KEY);
      if (token) {
        try {
          const decodedToken: any = this.decodeToken(token);
          console.log('AuthService: checkInitialLoginStatus - 解碼後的 JWT Payload:', decodedToken); // 新增日誌

          const username = decodedToken?.sub;
          // ⚠️ 重要：這裡的 'roles' 字段名必須與後端 JWT Payload 中的角色字段名一致
          // 如果後端是 'authorities' 或其他，請修改這裡
          const roles = Array.isArray(decodedToken?.roles) ? decodedToken.roles : [];

          if (username) {
            this.setLoginStatus(true, username, roles);
            console.log('AuthService: 應用程式啟動時，從 localStorage 恢復登入狀態。用戶:', username, '角色:', roles);
          } else {
            console.warn('AuthService: 應用程式啟動時找到 token，但無法從 payload 解析用戶名。將執行登出。');
            this.logout();
          }
        } catch (e) {
          console.error('AuthService: 應用程式啟動時，解析 token 錯誤:', e);
          this.logout();
        }
      } else {
        console.log('AuthService: 應用程式啟動時，localStorage 中沒有找到 token。');
      }
    }
  }

  private decodeToken(token: string): any | null {
    try {
      const base64Url = token.split('.')[1];
      const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
      const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
          return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
      }).join(''));
      const parsedPayload = JSON.parse(jsonPayload);
      console.log('AuthService: decodeToken - 原始 JWT Payload (JSON):', parsedPayload); // 新增日誌
      return parsedPayload;
    } catch (e) {
      console.error('AuthService: 解析 JWT 失敗:', e);
      return null;
    }
  }

  login(token: string, usernameFromLogin?: string): void {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.setItem(this.TOKEN_KEY, token);
      console.log('AuthService: Token 已儲存到 localStorage:', token); // 新增日誌

      let resolvedUsername: string | null = null;
      let resolvedRoles: string[] = [];

      if (usernameFromLogin) {
        resolvedUsername = usernameFromLogin;
        console.log('AuthService: 登入時直接使用提供的用戶名:', resolvedUsername);
      }

      try {
        const decodedToken = this.decodeToken(token);
        console.log('AuthService: login - 解碼後的 JWT Payload:', decodedToken); // 新增日誌

        if (!resolvedUsername) {
          resolvedUsername = decodedToken?.sub || null;
        }
        // ⚠️ 重要：這裡的 'roles' 字段名必須與後端 JWT Payload 中的角色字段名一致
        resolvedRoles = Array.isArray(decodedToken?.roles) ? decodedToken.roles : [];
        console.log('AuthService: 登入時從 token 解析用戶名:', resolvedUsername, '角色:', resolvedRoles);
      } catch (e) {
        console.error('AuthService: 登入時無法從 token 解析用戶名或角色:', e);
      }

      this.setLoginStatus(true, resolvedUsername, resolvedRoles);
      console.log('AuthService: 登入成功！設定狀態。');

      setTimeout(() => {
        this.router.navigate(['/home']);
        console.log('AuthService: 導航至 /home (通過 setTimeout)。');
      }, 0);
    }
  }

  public setLoginStatus(loggedIn: boolean, username: string | null, roles: string[]): void {
    this._isLoggedIn.next(loggedIn);
    this._username.next(username);
    this._userRoles.next(roles);
    console.log(`AuthService: setLoginStatus 被呼叫。登入狀態: ${loggedIn}, 用戶名: ${username}, 角色: ${JSON.stringify(roles)}`); // 角色陣列 JSON 化
  }

  logout(): void {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.removeItem(this.TOKEN_KEY);
      console.log('AuthService: Token 已從 localStorage 移除。');
    }
    this.setLoginStatus(false, null, []);
    console.log('AuthService: 登出成功！導航至 /login。');
    this.router.navigate(['/login']);
  }

  getUserInitial(): string | null {
    const currentUsername = this._username.value;
    return currentUsername ? currentUsername.charAt(0).toUpperCase() : null;
  }

  getToken(): string | null {
    if (isPlatformBrowser(this.platformId)) {
      return localStorage.getItem(this.TOKEN_KEY);
    }
    return null;
  }

  hasRole(role: string): boolean {
    const currentRoles = this._userRoles.value;
    const isLoggedIn = this._isLoggedIn.value;
    const hasSpecificRole = currentRoles.includes(role.toUpperCase()); // 確保比較時都是大寫

    console.log(`AuthService: hasRole('${role}') 檢查。登入狀態: ${isLoggedIn}, 當前角色: ${JSON.stringify(currentRoles)}, 是否具備 '${role.toUpperCase()}' 角色: ${hasSpecificRole}`); // 新增日誌
    return isLoggedIn && hasSpecificRole;
  }

  getUserRoles(): string[] {
    return this._userRoles.value;
  }
}
