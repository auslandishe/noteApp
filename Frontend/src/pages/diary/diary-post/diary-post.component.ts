import {
  DiaryEntry,
  DiaryService,
  CreateDiaryRequest,
  Tag,
} from '../../../services/diary.service';
import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router'; // 加上這行

@Component({
  selector: 'app-post-article',
  templateUrl: './diary-post.component.html',
  imports: [CommonModule, FormsModule],
  standalone: true,
})
export class DiaryPostComponent implements OnInit {
  diaryId: number | null = null; // ✨ 新增：判斷是否為編輯模式
  postTitle: string = '';
  postAuthor: string = '';
  postContent: string = '';
  customTagInput: string = '';

  titleCharCount: number = 0;
  contentCharCount: number = 0;

  isModalVisible: boolean = false;
  modalMessage: string = '';

  availableTags: Tag[] = []; // 從後端載入的標籤
  selectedTags: string[] = [];

  tagColors: string[] = [
    'bg-blue-500',
    'bg-green-500',
    'bg-red-500',
    'bg-yellow-500',
    'bg-indigo-500',
    'bg-purple-500',
    'bg-pink-500',
    'bg-teal-500',
  ];

  constructor(
    private router: Router,
    private diaryService: DiaryService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.loadAvailableTags(); // 從後端載入標籤
    // ✨ 如果有 diaryId，載入資料進行編輯
    this.route.paramMap.subscribe((params) => {
      const idParam = params.get('id');
      if (idParam) {
        this.diaryId = +idParam;
        this.diaryService.getDiaryById(this.diaryId).subscribe({
          next: (entry) => {
            this.postTitle = entry.title;
            this.postAuthor = entry.author;
            this.postContent = entry.content;
            this.selectedTags = entry.tags;
            this.titleCharCount = this.postTitle.length;
            this.contentCharCount = this.postContent.length;
          },
          error: (err) => {
            console.error('載入日記失敗:', err);
            this.modalMessage = '載入日記資料失敗';
            this.isModalVisible = true;
          },
        });
      }
    });
  }

  /**
   * 從後端載入可用標籤
   */
  private loadAvailableTags(): void {
    this.diaryService.getAvailableTags().subscribe({
      next: (tags: Tag[]) => {
        this.availableTags = tags;
      },
      error: (error) => {
        console.error('載入標籤失敗:', error);
        this.modalMessage = '載入標籤失敗，請稍後再試。';
        this.isModalVisible = true;
      },
    });
  }

  onTitleInput(): void {
    this.titleCharCount = this.postTitle.length;
  }

  onContentInput(): void {
    this.contentCharCount = this.postContent.length;
  }

  addExistingTag(tag: string): void {
    if (!this.selectedTags.includes(tag)) {
      this.selectedTags.push(tag);
    }
  }

  addCustomTag(): void {
    const tag = this.customTagInput.trim();
    if (tag && !this.selectedTags.includes(tag)) {
      this.selectedTags.push(tag);
      this.customTagInput = '';
    }
  }

  removeTag(tagToRemove: string): void {
    this.selectedTags = this.selectedTags.filter((tag) => tag !== tagToRemove);
  }

  getTagColor(tag: string): string {
    // 檢查標籤是否在 availableTags 中，並使用其顏色
    const availableTag = this.availableTags.find((t) => t.name === tag);
    if (availableTag) {
      return availableTag.color;
    }

    // 否則，使用原始的 hash 算法
    let hash = 0;
    for (let i = 0; i < tag.length; i++) {
      hash = tag.charCodeAt(i) + ((hash << 5) - hash);
    }
    const index = Math.abs(hash % this.tagColors.length);
    return this.tagColors[index];
  }

  /**
   * 發布文章
   */
  publishArticle(): void {
    const title = this.postTitle.trim();
    const author = this.postAuthor.trim();
    const content = this.postContent.trim();

    if (title === '' || content === '') {
      this.modalMessage = '文章標題和內容不能為空！';
      this.isModalVisible = true;
      return;
    }

    const request: CreateDiaryRequest = {
      title: title,
      author: author,
      content: content,
      tags: this.selectedTags,
    };

    const saveObservable = this.diaryId
      ? this.diaryService.updateDiary(this.diaryId, request) // 編輯
      : this.diaryService.createDiary(request); // 新增

    saveObservable.subscribe({
      next: (response) => {
        this.modalMessage = this.diaryId
          ? '文章更新成功！'
          : '文章已成功發布！';
        this.isModalVisible = true;

        setTimeout(() => {
          this.closeModal();
          this.router.navigate(['/diary']);
        }, 1500);
      },
      error: (error) => {
        console.error('儲存文章失敗:', error);
        this.modalMessage = '儲存文章失敗，請稍後再試。';
        this.isModalVisible = true;
      },
    });
  }

  /**
   * 取消發布
   */
  cancelPost(): void {
    this.clearForm();
    this.modalMessage = '已取消發布，內容已清空。';
    this.isModalVisible = true;

    setTimeout(() => {
      this.closeModal();
      this.router.navigate(['/diary']);
    }, 1000);
  }

  closeModal(): void {
    this.isModalVisible = false;
  }

  private clearForm(): void {
    this.postTitle = '';
    this.postContent = '';
    this.customTagInput = '';
    this.selectedTags = [];
    this.titleCharCount = 0;
    this.contentCharCount = 0;
  }
}
