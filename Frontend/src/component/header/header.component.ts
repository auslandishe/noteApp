// src/app/header/header.component.ts
import { Router } from '@angular/router';
import { Component, OnInit, ChangeDetectionStrategy } from '@angular/core';
import { CommonModule, AsyncPipe } from '@angular/common';
import { AuthService } from '../../services/auth.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'AppHeader',
  standalone: true,
  imports: [CommonModule, AsyncPipe],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush // 確保使用 OnPush 策略
})
export class HeaderComponent implements OnInit {
  isLoggedIn$: Observable<boolean>;
  username$: Observable<string | null>;

  constructor(
    private router: Router,
    private authService: AuthService
  ) {
    this.isLoggedIn$ = this.authService.isLoggedIn$;
    this.username$ = this.authService.username$;
    console.log('HeaderComponent: Constructor called. 已訂閱 AuthService 的 Observable。');
  }

  ngOnInit(): void {
    console.log('HeaderComponent: ngOnInit called.');
    this.isLoggedIn$.subscribe(loggedIn => console.log('HeaderComponent: isLoggedIn$ 變化為:', loggedIn));
    this.username$.subscribe(username => console.log('HeaderComponent: username$ 變化為:', username));
  }

  redirectToHome(): void {
    this.router.navigate(['/home']);
  }

  onLoginClick(): void {
    console.log('HeaderComponent: 登入按鈕被點擊！導航至 /login。');
    this.router.navigate(['/login']);
  }

  onLogoutClick(): void {
    console.log('HeaderComponent: 登出按鈕被點擊！');
    this.authService.logout();
  }

  getUserInitial(): string | null {
    return this.authService.getUserInitial();
  }
}
