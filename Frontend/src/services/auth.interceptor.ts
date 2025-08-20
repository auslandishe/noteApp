import { HttpInterceptorFn } from '@angular/common/http';
import {  PLATFORM_ID, inject } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';

export const authInterceptorFn: HttpInterceptorFn = (req, next) => {
  // 注入 PLATFORM_ID 來判斷當前程式運行的平台
  const platformId = inject(PLATFORM_ID);

  let token: string | null = null;

  // ⚠️ 只有在瀏覽器環境中才嘗試存取 localStorage
  if (isPlatformBrowser(platformId)) {
    token = localStorage.getItem('token');
  }

  // 如果 token 存在，將其添加到 Authorization 標頭
  if (token) {
    const cloned = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    return next(cloned);
  }

  // 如果沒有 token (或在伺服器端)，直接發送請求
  return next(req);
};
