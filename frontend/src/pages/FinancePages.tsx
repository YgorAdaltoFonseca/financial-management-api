import { useCallback, useEffect, useMemo, useState } from 'react'
import { ArrowDownLeft, ArrowUpRight, CalendarDays, ChartNoAxesCombined, ChevronDown, CircleDollarSign, Pencil, Plus, Search, Tags, Trash2, WalletCards } from 'lucide-react'
import { Area, AreaChart, CartesianGrid, Cell, Pie, PieChart, ResponsiveContainer, Tooltip, XAxis, YAxis } from 'recharts'
import { Link } from 'react-router-dom'
import { api, errorMessage } from '../api/client'
import type { Category, Dashboard, MonthlyDashboard, Subscription, Transaction, TransactionOrigin, TransactionType } from '../api/types'
import { EmptyState, ErrorBox, Loading, PageTitle, money } from './shared'

const date = (value: string) => new Intl.DateTimeFormat('pt-BR').format(new Date(value.length === 10 ? `${value}T12:00:00` : value))
const monthLabel = (value: string) => new Intl.DateTimeFormat('pt-BR', { month: 'short', year: '2-digit' }).format(new Date(`${value}-01T12:00:00`)).replace('.', '')
const chartColors = ['#25785a', '#6a9b7b', '#b5c7ae', '#c5a56a', '#8da9a1', '#a8b9cd', '#cf927b']
const transactionTypeName: Record<TransactionType, string> = { ENTRY: 'Entrada', EXIT: 'Despesa' }

function StatCard({ label, value, note, icon: Icon, tone = 'neutral' }: { label: string; value: string; note: string; icon: typeof WalletCards; tone?: 'positive' | 'negative' | 'neutral' }) { return <article className="stat-card"><div className="stat-top"><span>{label}</span><span className={`stat-icon ${tone}`}><Icon size={18} /></span></div><strong className="stat-value">{value}</strong><span className="stat-note">{note}</span></article> }

export function DashboardPage() {
  const [dashboard, setDashboard] = useState<Dashboard | null>(null)
  const [monthly, setMonthly] = useState<MonthlyDashboard[]>([])
  const [transactions, setTransactions] = useState<Transaction[]>([])
  const [subscriptions, setSubscriptions] = useState<Subscription[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [monthsShown, setMonthsShown] = useState('6')
  const load = useCallback(async () => {
    setLoading(true); setError('')
    try {
      const [totals, series, recent, recurring] = await Promise.all([
        api.get<Dashboard>('/api/dashboard/total'), api.get<MonthlyDashboard[]>('/api/dashboard/monthly'), api.get<Transaction[]>('/api/transaction/listTransaction'), api.get<Subscription[]>('/api/subscription/listSubscriptions'),
      ])
      setDashboard(totals.data); setMonthly(series.data); setTransactions(recent.data); setSubscriptions(recurring.data)
    } catch (e) { setError(errorMessage(e, 'Não foi possível carregar o painel.')) }
    finally { setLoading(false) }
  }, [])
  useEffect(() => { void load() }, [load])

  const series = useMemo(() => {
    const count = Number(monthsShown)
    const now = new Date()
    const lookup = new Map(monthly.map((item) => [item.month, item]))
    return Array.from({ length: count }, (_, offset) => {
      const dateValue = new Date(now.getFullYear(), now.getMonth() - count + offset + 1, 1)
      const key = `${dateValue.getFullYear()}-${String(dateValue.getMonth() + 1).padStart(2, '0')}`
      return { month: monthLabel(key), entries: lookup.get(key)?.entries ?? 0, exit: lookup.get(key)?.exit ?? 0 }
    })
  }, [monthly, monthsShown])
  const totalBalance = (dashboard?.totalEntries ?? 0) - (dashboard?.totalExit ?? 0)
  const recent = [...transactions].sort((a, b) => b.dateTime.localeCompare(a.dateTime)).slice(0, 5)
  const categoryMap = new Map<string, number>()
  for (const item of transactions) if (item.type === 'EXIT') categoryMap.set(item.categoryTypeName, (categoryMap.get(item.categoryTypeName) ?? 0) + item.value)
  const categoryData = [...categoryMap].map(([name, value]) => ({ name, value })).sort((a, b) => b.value - a.value)

  return <>
    <PageTitle eyebrow="SEU RESUMO" title="Visão geral" description="Acompanhe entradas, despesas e o ritmo das suas finanças." action={<div className="date-select"><CalendarDays size={16} /><select aria-label="Período do gráfico" value={monthsShown} onChange={(event) => setMonthsShown(event.target.value)}><option value="3">Últimos 3 meses</option><option value="6">Últimos 6 meses</option><option value="12">Últimos 12 meses</option><option value="24">Últimos 24 meses</option></select><ChevronDown size={14} /></div>} />
    {loading ? <Loading label="Atualizando seu resumo…" /> : error ? <ErrorBox message={error} retry={() => void load()} /> : <>
      <div className="stat-grid">
        <StatCard label="Saldo acumulado" value={money(totalBalance)} note="Entradas menos despesas cadastradas" icon={CircleDollarSign} tone={totalBalance >= 0 ? 'positive' : 'negative'} />
        <StatCard label="Total de entradas" value={money(dashboard?.totalEntries ?? 0)} note="Soma das entradas cadastradas" icon={ArrowDownLeft} tone="positive" />
        <StatCard label="Total de despesas" value={money(dashboard?.totalExit ?? 0)} note="Soma das despesas cadastradas" icon={ArrowUpRight} tone="negative" />
        <StatCard label="Assinaturas ativas" value={String(subscriptions.filter((item) => item.subscriptionStatus === 'ACTIVE').length)} note="Recorrências cadastradas" icon={WalletCards} />
      </div>
      <div className="dashboard-grid">
        <section className="panel chart-panel"><div className="panel-heading"><div><h2>Entradas e despesas</h2><p>Movimentação mensal no período</p></div><span className="chart-legend"><i className="legend-income" />Entradas <i className="legend-expense" />Despesas</span></div>
          {monthly.length === 0 ? <EmptyState title="Ainda sem movimentações" text="Quando você registrar transações, a evolução mensal aparecerá aqui." icon={ChartNoAxesCombined} /> : <div className="chart-wrap"><ResponsiveContainer width="100%" height="100%"><AreaChart data={series} margin={{ top: 8, right: 8, left: -12, bottom: 0 }}><defs><linearGradient id="incomeFill" x1="0" y1="0" x2="0" y2="1"><stop offset="0%" stopColor="#2e805f" stopOpacity={0.16} /><stop offset="95%" stopColor="#2e805f" stopOpacity={0} /></linearGradient><linearGradient id="expenseFill" x1="0" y1="0" x2="0" y2="1"><stop offset="0%" stopColor="#c87967" stopOpacity={0.13} /><stop offset="95%" stopColor="#c87967" stopOpacity={0} /></linearGradient></defs><CartesianGrid vertical={false} stroke="#eef0ed" /><XAxis dataKey="month" axisLine={false} tickLine={false} tick={{ fill: '#89918c', fontSize: 11 }} dy={10} /><YAxis axisLine={false} tickLine={false} tick={{ fill: '#89918c', fontSize: 11 }} tickFormatter={(value) => value >= 1000 ? `R$${(value / 1000).toFixed(0)}k` : `R$${value}`} /><Tooltip formatter={(value) => money(Number(value))} contentStyle={{ border: '1px solid #eaede9', borderRadius: 10, fontSize: 12 }} /><Area type="monotone" dataKey="entries" name="Entradas" stroke="#2e805f" strokeWidth={2.5} fill="url(#incomeFill)" /><Area type="monotone" dataKey="exit" name="Despesas" stroke="#c87967" strokeWidth={2.5} fill="url(#expenseFill)" /></AreaChart></ResponsiveContainer></div>}
        </section>
        <section className="panel category-panel"><div className="panel-heading"><div><h2>Despesas por categoria</h2><p>Distribuição dos lançamentos</p></div></div>
          {categoryData.length === 0 ? <EmptyState title="Sem despesas por categoria" text="As categorias aparecem aqui após os primeiros lançamentos." icon={Tags} /> : <><div className="pie-wrap"><ResponsiveContainer width="100%" height="100%"><PieChart><Pie data={categoryData} dataKey="value" nameKey="name" innerRadius={64} outerRadius={88} paddingAngle={3} stroke="none">{categoryData.map((entry, index) => <Cell key={entry.name} fill={chartColors[index % chartColors.length]} />)}</Pie><Tooltip formatter={(value) => money(Number(value))} contentStyle={{ border: '1px solid #eaede9', borderRadius: 10, fontSize: 12 }} /></PieChart></ResponsiveContainer></div><div className="category-legend">{categoryData.slice(0, 5).map((entry, index) => <div key={entry.name}><span><i style={{ background: chartColors[index % chartColors.length] }} />{entry.name}</span><strong>{money(entry.value)}</strong></div>)}</div></>}
        </section>
      </div>
      <section className="panel recent-panel"><div className="panel-heading"><div><h2>Transações recentes</h2><p>Seus últimos lançamentos</p></div><Link className="text-link" to="/transactions">Ver todas <ArrowUpRight size={14} /></Link></div>
        {recent.length === 0 ? <EmptyState title="Sua lista está vazia" text="Adicione sua primeira entrada ou despesa para começar a acompanhar o movimento." /> : <TransactionTable rows={recent} compact />}
      </section>
    </>}
  </>
}

function TransactionTable({ rows, compact = false, onEdit, onDelete }: { rows: Transaction[]; compact?: boolean; onEdit?: (transaction: Transaction) => void; onDelete?: (transaction: Transaction) => void }) {
  return <div className="table-scroll"><table className="data-table"><thead><tr><th>Descrição</th><th>Categoria</th><th>Data</th><th>Tipo</th><th className="align-right">Valor</th>{!compact && (onEdit || onDelete) && <th aria-label="Ações" />}</tr></thead><tbody>{rows.map((row) => <tr key={row.id}><td><div className="transaction-name"><span className={`transaction-dot ${row.type === 'ENTRY' ? 'dot-income' : 'dot-expense'}`}>{row.type === 'ENTRY' ? <ArrowDownLeft size={14} /> : <ArrowUpRight size={14} />}</span><span><strong>{row.description}</strong><small>{row.origin === 'MANUAL' ? 'Manual' : 'Bancária'}</small></span></div></td><td>{row.categoryTypeName}</td><td>{date(row.dateTime)}</td><td><span className={`pill ${row.type === 'ENTRY' ? 'pill-income' : 'pill-expense'}`}>{transactionTypeName[row.type]}</span></td><td className={`align-right amount ${row.type === 'ENTRY' ? 'amount-income' : ''}`}>{row.type === 'ENTRY' ? '+' : '−'} {money(row.value)}</td>{!compact && (onEdit || onDelete) && <td><div className="row-actions">{onEdit && <button className="icon-button" aria-label={`Editar ${row.description}`} onClick={() => onEdit(row)}><Pencil size={15} /></button>}{onDelete && <button className="icon-button danger-icon" aria-label={`Excluir ${row.description}`} onClick={() => onDelete(row)}><Trash2 size={15} /></button>}</div></td>}</tr>)}</tbody></table></div>
}

function TransactionForm({ categories, initial, onSave, onCancel, busy }: { categories: Category[]; initial?: Transaction; onSave: (form: { value: string; type: TransactionType; origin: TransactionOrigin; description: string; categoryId: string }) => Promise<void>; onCancel: () => void; busy: boolean }) {
  const [form, setForm] = useState({ value: initial ? String(initial.value) : '', type: initial?.type ?? 'EXIT' as TransactionType, origin: initial?.origin ?? 'MANUAL' as TransactionOrigin, description: initial?.description ?? '', categoryId: initial ? String(initial.categoryTypeId) : '' })
  const usable = categories.filter((category) => category.categoryType === 'BOTH' || category.categoryType === form.type)
  useEffect(() => { if (!usable.some((category) => String(category.id) === form.categoryId)) setForm((old) => ({ ...old, categoryId: usable[0] ? String(usable[0].id) : '' })) }, [usable, form.categoryId])
  return <form className="edit-form" onSubmit={(event) => { event.preventDefault(); void onSave(form) }}>
    <div className="form-grid"><label className="field"><span>Descrição</span><input required maxLength={255} value={form.description} onChange={(event) => setForm({ ...form, description: event.target.value })} placeholder="Ex.: Mercado, salário…" /></label><label className="field"><span>Valor em reais</span><input required type="number" min="0.01" step="0.01" value={form.value} onChange={(event) => setForm({ ...form, value: event.target.value })} placeholder="0,00" /></label><label className="field"><span>Tipo</span><select value={form.type} onChange={(event) => setForm({ ...form, type: event.target.value as TransactionType })}><option value="ENTRY">Entrada</option><option value="EXIT">Despesa</option></select></label><label className="field"><span>Categoria</span><select required value={form.categoryId} onChange={(event) => setForm({ ...form, categoryId: event.target.value })}><option value="">Selecione uma categoria</option>{usable.map((category) => <option key={category.id} value={category.id}>{category.name}</option>)}</select></label><label className="field"><span>Origem</span><select value={form.origin} onChange={(event) => setForm({ ...form, origin: event.target.value as TransactionOrigin })}><option value="MANUAL">Manual</option><option value="BANKING">Bancária</option></select></label></div>
    {categories.length === 0 && <p className="inline-note">Crie uma categoria antes de registrar transações.</p>}
    <div className="form-actions"><button type="button" className="button button-quiet" onClick={onCancel}>Cancelar</button><button className="button button-primary" disabled={busy || usable.length === 0}>{busy ? 'Salvando…' : initial ? 'Salvar alterações' : 'Adicionar transação'}</button></div>
  </form>
}

export function TransactionsPage() {
  const [transactions, setTransactions] = useState<Transaction[]>([])
  const [categories, setCategories] = useState<Category[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [query, setQuery] = useState('')
  const [type, setType] = useState('ALL')
  const [categoryId, setCategoryId] = useState('ALL')
  const [dateFrom, setDateFrom] = useState('')
  const [dateTo, setDateTo] = useState('')
  const [editing, setEditing] = useState<Transaction | null | 'new'>(null)
  const [busy, setBusy] = useState(false)
  const load = useCallback(async () => { setLoading(true); setError(''); try { const [tx, cats] = await Promise.all([api.get<Transaction[]>('/api/transaction/listTransaction'), api.get<Category[]>('/api/category/categoriesList')]); setTransactions(tx.data); setCategories(cats.data) } catch (e) { setError(errorMessage(e, 'Não foi possível carregar as transações.')) } finally { setLoading(false) } }, [])
  useEffect(() => { void load() }, [load])
  const filtered = transactions.filter((row) => {
    const rowDate = row.dateTime.slice(0, 10)
    return (type === 'ALL' || row.type === type) && (categoryId === 'ALL' || String(row.categoryTypeId) === categoryId) && (!dateFrom || rowDate >= dateFrom) && (!dateTo || rowDate <= dateTo) && `${row.description} ${row.categoryTypeName}`.toLocaleLowerCase('pt-BR').includes(query.toLocaleLowerCase('pt-BR'))
  }).sort((a, b) => b.dateTime.localeCompare(a.dateTime))
  const save = async (form: { value: string; type: TransactionType; origin: TransactionOrigin; description: string; categoryId: string }) => { setBusy(true); setError(''); try { const body = { value: Number(form.value), type: form.type, origin: form.origin, description: form.description.trim() }; if (editing === 'new') await api.post(`/api/transaction/createTransaction/${form.categoryId}`, body); else if (editing) await api.put(`/api/transaction/updateTransaction/${editing.id}/${form.categoryId}`, body); setEditing(null); await load() } catch (e) { setError(errorMessage(e, 'Não foi possível salvar a transação.')) } finally { setBusy(false) } }
  const remove = async (row: Transaction) => { if (!window.confirm(`Excluir “${row.description}”?`)) return; setError(''); try { await api.delete(`/api/transaction/delete/${row.id}`); await load() } catch (e) { setError(errorMessage(e, 'Não foi possível excluir a transação.')) } }
  return <><PageTitle eyebrow="MOVIMENTAÇÕES" title="Transações" description="Entradas e despesas registradas na sua conta." action={<button className="button button-primary" onClick={() => setEditing('new')}><Plus size={17} /> Nova transação</button>} />
    {error && <ErrorBox message={error} retry={() => void load()} />}
    {editing !== null && <section className="panel editor-panel"><div className="panel-heading"><div><h2>{editing === 'new' ? 'Nova transação' : 'Editar transação'}</h2><p>Preencha os dados do lançamento.</p></div></div><TransactionForm key={editing === 'new' ? 'new' : editing.id} categories={categories} initial={editing === 'new' ? undefined : editing} onSave={save} onCancel={() => setEditing(null)} busy={busy} /></section>}
    <section className="panel list-panel"><div className="filter-row"><label className="search-field"><Search size={17} /><input aria-label="Buscar transações" value={query} onChange={(event) => setQuery(event.target.value)} placeholder="Buscar por descrição ou categoria" /></label><label className="filter-select"><span>Tipo</span><select aria-label="Filtrar tipo" value={type} onChange={(event) => setType(event.target.value)}><option value="ALL">Todos os tipos</option><option value="ENTRY">Entradas</option><option value="EXIT">Despesas</option></select></label><label className="filter-select"><span>Categoria</span><select aria-label="Filtrar categoria" value={categoryId} onChange={(event) => setCategoryId(event.target.value)}><option value="ALL">Todas</option>{categories.map((category) => <option key={category.id} value={category.id}>{category.name}</option>)}</select></label><label className="filter-date"><span>De</span><input aria-label="Data inicial" type="date" value={dateFrom} onChange={(event) => setDateFrom(event.target.value)} /></label><label className="filter-date"><span>Até</span><input aria-label="Data final" type="date" value={dateTo} onChange={(event) => setDateTo(event.target.value)} /></label><span className="results-count">{filtered.length} {filtered.length === 1 ? 'lançamento' : 'lançamentos'}</span></div>
      {loading ? <Loading /> : filtered.length === 0 ? <EmptyState title={transactions.length ? 'Nenhum resultado' : 'Nenhuma transação ainda'} text={transactions.length ? 'Ajuste a busca ou os filtros para ver outros lançamentos.' : 'Adicione seu primeiro lançamento para acompanhar as movimentações.'} action={!transactions.length && <button className="button button-secondary" onClick={() => setEditing('new')}><Plus size={16} /> Criar transação</button>} /> : <TransactionTable rows={filtered} onEdit={setEditing} onDelete={(row) => void remove(row)} />}
    </section></>
}
