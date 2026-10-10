export type User = { id: number; name: string; email: string }
export type LoginResponse = { token: string }
export type CategoryType = 'ENTRY' | 'EXIT' | 'BOTH'
export type Category = { id: number; name: string; categoryType: CategoryType }
export type TransactionType = 'ENTRY' | 'EXIT'
export type TransactionOrigin = 'BANKING' | 'MANUAL'
export type Transaction = {
  id: number; value: number; dateTime: string; type: TransactionType; origin: TransactionOrigin;
  description: string; categoryTypeId: number; categoryTypeName: string; userId: number; userName: string
}
export type TransactionRequest = { value: number; type: TransactionType; origin: TransactionOrigin; description: string }
export type Dashboard = { totalEntries: number; totalExit: number; totalSubscriptions: number }
export type MonthlyDashboard = { month: string; entries: number; exit: number }
export type Frequency = 'WEEKLY' | 'MONTHLY' | 'QUARTERLY' | 'SEMIANNUAL' | 'ANNUAL'
export type SubscriptionStatus = 'ACTIVE' | 'CANCELLED'
export type Subscription = {
  id: number; name: string; value: number; frequency: Frequency; startDate: string; nextCharge: string;
  subscriptionStatus: SubscriptionStatus; userId: number; userName: string; categoryTypeId: number; categoryTypeName: string
}
