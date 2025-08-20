// src/app/models/auth-response.dto.ts
export interface AuthResponseDto {
  token: string;
  tokenType?: string; // 若後端有回傳，否則可忽略
}

