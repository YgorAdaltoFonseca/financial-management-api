import type { ReactNode } from 'react'
import { LoaderCircle, WalletCards } from 'lucide-react'

export const money = (value: number) => new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(value || 0)
export function PageTitle({ eyebrow, title, description, action }: { eyebrow: string; title: string; description: string; action?: ReactNode }) {
  return <div className="page-title-row"><div><div className="eyebrow">{eyebrow}</div><h1>{title}</h1><p className="muted">{description}</p></div>{action}</div>
}
export function Loading({ label = 'Carregando informações…' }: { label?: string }) { return <div className="loading-state" role="status"><LoaderCircle className="spin" size={19} />{label}</div> }
export function ErrorBox({ message, retry }: { message: string; retry?: () => void }) { return <div className="alert alert-error" role="alert"><span>{message}</span>{retry && <button className="button button-quiet button-small" onClick={retry}>Tentar novamente</button>}</div> }
export function EmptyState({ title, text, icon: Icon = WalletCards, action }: { title: string; text: string; icon?: typeof WalletCards; action?: ReactNode }) { return <div className="empty-state"><span className="empty-icon"><Icon size={21} /></span><strong>{title}</strong><p>{text}</p>{action}</div> }
