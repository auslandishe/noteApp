import { CommonModule } from '@angular/common';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, NavigationEnd, RouterLink, RouterOutlet } from '@angular/router';
import { DiaryEntry, DiaryService } from '../../services/diary.service';
import { Subscription, filter } from 'rxjs';

@Component({
  selector: 'app-diary',
  imports: [CommonModule, RouterLink],
  templateUrl: './diary.component.html',
  styleUrl: './diary.component.css'
})
export class DiaryComponent implements OnInit, OnDestroy {
  myDiaries: DiaryEntry[] = [];
  private entriesSubscription: Subscription | undefined;
  private routerEventsSubscription: Subscription | undefined;

  constructor(public router: Router, private diaryService: DiaryService) { }

  ngOnInit(): void {
    // 監聽路由事件，當路由變更完成時，重新載入日記列表
    this.routerEventsSubscription = this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe(() => {
        // 只有當位於根路由 /diary 時才載入數據
        if (this.router.url === '/diary' || this.router.url === '/diary/') {
          this.loadMyDiaries();
        }
      });

    // 元件初始化時，載入一次日記列表
    this.loadMyDiaries();
  }

  ngOnDestroy(): void {
    // 取消所有訂閱以防止內存洩漏
    if (this.entriesSubscription) {
      this.entriesSubscription.unsubscribe();
    }
    if (this.routerEventsSubscription) {
      this.routerEventsSubscription.unsubscribe();
    }
  }

  /**
   * 根據後端 API 載入當前使用者的所有日記
   */
  loadMyDiaries(): void {
    this.entriesSubscription = this.diaryService.getMyDiaries().subscribe({
      next: (entries) => {
        this.myDiaries = entries;
        console.log('日記條目已成功載入:', this.myDiaries);
      },
      error: (err) => {
        console.error('載入日記時發生錯誤:', err);
      }
    });
  }

  /**
   * 根據標籤內容獲取 Tailwind CSS 顏色類別
   */
  getTagColor(tag: string): string {
    const tagColors: string[] = [
      'bg-blue-500', 'bg-green-500', 'bg-red-500', 'bg-yellow-500',
      'bg-indigo-500', 'bg-purple-500', 'bg-pink-500', 'bg-teal-500'
    ];
    let hash = 0;
    for (let i = 0; i < tag.length; i++) {
      hash = tag.charCodeAt(i) + ((hash << 5) - hash);
    }
    const index = Math.abs(hash % tagColors.length);
    return tagColors[index];
  }
}
