import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router'; // 引入 Router 服務用於導航
import { AuthService } from '../../services/auth.service';

interface NavItem {
  label: string;
  icon: string; // Using string to represent icon names
  route: string;
}

@Component({
  selector: 'AppSidebar',
  imports: [RouterLink, RouterLinkActive, CommonModule],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css',
})
export class SidebarComponent {
  // Define navigation items with labels, icon names, and routes
  navItems: NavItem[] = [
    { label: '首頁', icon: 'home', route: '/home' },
    { label: '日記', icon: 'diary', route: '/diary' },
    { label: '便利貼', icon: 'note', route: '/sticky-note' },
    { label: '標籤管理', icon: 'tags', route: '/tag-management' },
    { label: '使用者管理', icon: 'users', route: '/user-management' }, // <-- 新增使用者管理連結
  ];

  constructor(private router: Router, private authService: AuthService) {} // 注入 Router 服務

  ngOnInit(): void {
    // Component initialization logic if needed
  }

  // Handle logout button click
  onLogout(): void {
    console.log('HeaderComponent: 登出按鈕被點擊！');
    this.authService.logout();
  }
}
