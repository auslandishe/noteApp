import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';

import { DiaryEntry, DiaryService } from '../../../services/diary.service';

@Component({
  selector: 'app-diary-detail',
  templateUrl: './diary-detail.component.html',
  standalone: true,
  imports: [CommonModule],
})
export class DiaryDetailComponent implements OnInit {
  diaryEntry: DiaryEntry | null = null;
  isLoading: boolean = true;
  errorMessage: string | null = null;
  isDeleting: boolean = false;
  tagColorMap: { [key: string]: string } = {}; // 新增 tagColorMap 屬性

  // 一組預設的顏色，用來為標籤生成顏色
  private tagColors: string[] = [
    'blue',
    'green',
    'red',
    'yellow',
    'indigo',
    'purple',
    'pink',
    'teal',
  ];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private diaryService: DiaryService
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      const idString = params.get('id');
      if (idString) {
        const id = parseInt(idString, 10);
        if (!isNaN(id)) {
          this.fetchDiaryEntry(id);
        } else {
          this.errorMessage = '無效的日記ID。';
          this.isLoading = false;
        }
      } else {
        this.errorMessage = '找不到日記ID。';
        this.isLoading = false;
      }
    });
  }

  /**
   * 根據 ID 從服務中獲取日記資料
   * @param id 日記條目 ID
   */
  fetchDiaryEntry(id: number): void {
    this.isLoading = true;
    this.diaryService.getDiaryById(id).subscribe({
      next: (entry) => {
        this.diaryEntry = entry;
        this.isLoading = false;
        this.generateTagColorMap(entry.tags); // 在獲取日記後生成標籤顏色對應
      },
      error: (error) => {
        console.error('載入日記詳情失敗:', error);
        this.errorMessage = '載入日記詳情失敗，請確認該日記是否存在。';
        this.isLoading = false;
      },
    });
  }

  /**
   * 為每個標籤生成一個唯一的顏色，並存儲在 tagColorMap 中
   * @param tags 標籤陣列
   */
  private generateTagColorMap(tags: string[]): void {
    tags.forEach((tag) => {
      if (!this.tagColorMap[tag]) {
        // 使用簡單的 hash 算法為標籤選擇顏色
        let hash = 0;
        for (let i = 0; i < tag.length; i++) {
          hash = tag.charCodeAt(i) + ((hash << 5) - hash);
        }
        const index = Math.abs(hash % this.tagColors.length);
        this.tagColorMap[tag] = this.tagColors[index];
      }
    });
  }

  /**
   * 刪除日記條目
   */
  deleteDiary(): void {
    if (
      this.diaryEntry &&
      confirm(`確定要刪除這篇標題為「${this.diaryEntry.title}」的日記嗎？`)
    ) {
      this.isDeleting = true;
      this.diaryService.deleteDiary(this.diaryEntry.id).subscribe({
        next: () => {
          console.log('日記已成功刪除');
          this.router.navigate(['/diary']);
        },
        error: (error) => {
          console.error('刪除日記失敗:', error);
          this.errorMessage = '刪除日記失敗，請稍後再試。';
          this.isDeleting = false;
        },
      });
    }
  }

  goToEdit(): void {
    if (this.diaryEntry) {
      this.router.navigate(['/diary/post', this.diaryEntry.id]);
    }
  }

  /**
   * 返回日記列表頁面
   */
  goBack(): void {
    this.router.navigate(['/diary']);
  }
}
