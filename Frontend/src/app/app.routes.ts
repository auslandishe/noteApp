// src/app/routes.ts
import { Routes } from '@angular/router';
import { TagManagementComponent } from '../pages/tag-management/tag-management.component';
import { StickyNoteComponent } from '../pages/sticky-note/sticky-note.component';
import { HomeComponent } from '../pages/home/home.component';
import { LoginComponent } from '../pages/login/login.component';
import { RegisterComponent } from '../pages/register/register.component';
import { UserManagementComponent } from '../pages/user-management/user-management.component';
import { AuthGuard } from '../security/auth.guard';
import { AdminGuard } from '../security/admin.guard';
import { DiaryPostComponent } from '../pages/diary/diary-post/diary-post.component';
import { DiaryComponent } from '../pages/diary/diary.component';
import { DiaryDetailComponent } from '../pages/diary/diary-detail/diary-detail.component';

export const routes: Routes = [
  { path: '', component: HomeComponent, canActivate: [AuthGuard] },
  { path: 'home', component: HomeComponent, canActivate: [AuthGuard] },

  // 日記總覽頁
  { path: 'diary', component: DiaryComponent, canActivate: [AuthGuard] },

  // 新增或編輯日記文章（帶 id）
  { path: 'diary/post', component: DiaryPostComponent, canActivate: [AuthGuard] },

  // 查看日記文章細節（用 id 查文章）
  { path: 'diary/:id', component: DiaryDetailComponent, canActivate: [AuthGuard] },

  // 其他功能頁
  { path: 'sticky-note', component: StickyNoteComponent, canActivate: [AuthGuard] },
  { path: 'tag-management', component: TagManagementComponent, canActivate: [AuthGuard] },

  // 認證相關（不需要登入）
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },

  // 使用者管理，只限管理員
  { path: 'user-management', component: UserManagementComponent, canActivate: [AdminGuard] },

  // 預設轉址（可選）
  { path: '**', redirectTo: '', pathMatch: 'full' },
];
