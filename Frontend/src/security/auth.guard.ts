import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import {
  CanActivate,
  ActivatedRouteSnapshot,
  RouterStateSnapshot,
  Router
} from '@angular/router';
import { isPlatformBrowser } from '@angular/common';

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {

  constructor(
    @Inject(PLATFORM_ID) private platformId: Object,
    private router: Router
  ) {}

  canActivate(
    _route: ActivatedRouteSnapshot,
    _state: RouterStateSnapshot
  ): boolean {
     console.log('[AuthGuard] 守衛執行');
    // 1. SSR 時直接放行
    if (!isPlatformBrowser(this.platformId)) {
      return true;
    }

    // 2. 檢查 localStorage 中是否有 token
    const token = localStorage.getItem('token');
    if (!token) {
      alert('請先登入');
      this.router.navigate(['/login']);
      return false;
    }

    // 3. 有 token 代表已登入，放行
    return true;
  }
}
