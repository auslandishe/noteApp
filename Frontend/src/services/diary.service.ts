import { Inject, Injectable, PLATFORM_ID } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { isPlatformBrowser } from '@angular/common';
// 定義後端 API 回傳的日記條目數據結構
export interface DiaryEntry {
  id: number;
  title: string;
  author: string;
  content: string;
  userId: number;
  tags: string[];
  createdAt: string;
  updatedAt: string;
}

// 定義新增與更新日記的請求體數據結構
// 通常更新請求的資料結構與新增請求相同
export interface CreateDiaryRequest {
  title: string;
  content: string;
  author: string;
  tags: string[];
}

// 定義標籤數據結構
export interface Tag {
  name: string;
  color: string;
}

@Injectable({
  providedIn: 'root',
})
export class DiaryService {
  private apiUrl = 'http://localhost:8080/api/diaries';

  constructor(
    private http: HttpClient,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  /**
   * 建立一個 HttpHeaders 物件，包含授權標頭
   * @returns {HttpHeaders} 包含授權權杖的 HttpHeaders
   */
  private getAuthHeaders(): HttpHeaders {
    // 確保只在瀏覽器執行，SSR（Server-side）不會觸發
    if (isPlatformBrowser(this.platformId)) {
      const token = localStorage.getItem('token');
      if (token) {
        return new HttpHeaders({
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        });
      }
    }

    // SSR 或沒有 token 時回傳空 headers（或預設 Content-Type）
    return new HttpHeaders({
      'Content-Type': 'application/json',
    });
  }

  /**
   * 取得目前使用者所有的日記條目
   * 對應後端 GET /api/diaries
   */
  getMyDiaries(): Observable<DiaryEntry[]> {
    return this.http.get<DiaryEntry[]>(this.apiUrl, {
      headers: this.getAuthHeaders(),
    });
  }

  /**
   * 根據 ID 取得單一日記條目
   * 對應後端 GET /api/diaries/{id}
   * @param id 日記條目 ID
   */
  getDiaryById(id: number): Observable<DiaryEntry> {
    const url = `${this.apiUrl}/${id}`;
    return this.http.get<DiaryEntry>(url, { headers: this.getAuthHeaders() });
  }

  /**
   * 建立一個新的日記條目
   * 對應後端 POST /api/diaries
   * @param request 包含標題、內容和標籤的請求體
   */
  createDiary(request: CreateDiaryRequest): Observable<DiaryEntry> {
    return this.http.post<DiaryEntry>(this.apiUrl, request, {
      headers: this.getAuthHeaders(),
    });
  }

  /**
   * 更新一個日記條目
   * 對應後端 PUT /api/diaries/{id}
   * @param id 日記條目 ID
   * @param request 包含標題、內容和標籤的請求體
   */
  updateDiary(id: number, request: CreateDiaryRequest): Observable<DiaryEntry> {
    const url = `${this.apiUrl}/${id}`;
    return this.http.put<DiaryEntry>(url, request, {
      headers: this.getAuthHeaders(),
    });
  }

  /**
   * 根據 ID 刪除一個日記條目
   * 對應後端 DELETE /api/diaries/{id}
   * @param id 日記條目 ID
   */
  deleteDiary(id: number): Observable<void> {
    const url = `${this.apiUrl}/${id}`;
    return this.http.delete<void>(url, { headers: this.getAuthHeaders() });
  }

  /**
   * 取得後端所有可用的標籤
   * 對應後端 GET /api/tags
   */
  getAvailableTags(): Observable<Tag[]> {
    return this.http.get<Tag[]>('http://localhost:8080/api/tags', {
      headers: this.getAuthHeaders(),
    });
  }
}
