import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  imports: [],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
  constructor(private router: Router) { } // 注入 Router 服務

  ngOnInit(): void {
    // 元件初始化邏輯
  }

  // 點擊 "開始寫作" 按鈕的處理函數
  startWriting(): void {
    console.log('開始寫作按鈕被點擊');
    // 導航到日記或筆記編輯頁面
    this.router.navigate(['/diary']); // 假設您的日記編輯頁面路由是 '/diary/new'
  }

  // 點擊 "探索功能" 按鈕的處理函數
  exploreFeatures(): void {
    console.log('探索功能按鈕被點擊');
    // 導航到介紹功能或Dashboard頁面
    this.router.navigate(['/features']); // 假設您的功能介紹頁面路由是 '/features'
  }
}
