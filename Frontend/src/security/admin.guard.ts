// src/app/service/admin.guard.ts
import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from '../services/auth.service';// 確保路徑正確

@Injectable({ providedIn: 'root' })
export class AdminGuard implements CanActivate {
  constructor(
    private authService: AuthService,
    private router: Router,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    console.log('AdminGuard: canActivate 被呼叫。嘗試訪問路徑:', state.url); // 新增日誌

    if (isPlatformBrowser(this.platformId)) {
      const isLoggedIn = this.authService.isLoggedIn$;
      const userRoles = this.authService.getUserRoles();
      const hasAdminRole = this.authService.hasRole('ROLE_ADMIN');

      console.log(`AdminGuard: 檢查。登入狀態: ${isLoggedIn}, 用戶角色: ${JSON.stringify(userRoles)}, 是否具備 ADMIN 角色: ${hasAdminRole}`); // 新增日誌

      // 1. 檢查是否登入
      if (!isLoggedIn) {
        console.warn('AdminGuard: 用戶未登入，重定向到登入頁面。');
        alert('請先登入才能訪問此頁面！');
        return this.router.createUrlTree(['/login']);
      }

      // 2. 檢查是否具備 'ADMIN' 角色
      if (hasAdminRole) {
        console.log('AdminGuard: 用戶具備 ADMIN 角色，允許訪問。');
        return true;
      } else {
        console.warn('AdminGuard: 用戶未具備 ADMIN 角色，拒絕訪問。重定向到首頁。');
        alert('您沒有權限訪問此頁面！');
        return this.router.createUrlTree(['/home']);
      }
    }
    console.log('AdminGuard: 在伺服器端運行，預設允許訪問。');
    return true;
  }
}
